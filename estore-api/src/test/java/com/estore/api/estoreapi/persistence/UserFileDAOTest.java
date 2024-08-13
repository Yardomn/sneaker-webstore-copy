package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistance.ProductFileDAO;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistance.UserFileDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User File DAO class
 * 
 * @author SWEN Faculty
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO UserFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        testUsers[0] = new User(99,"Joe", true, new Product[20]);
        testUsers[1] = new User(100,"John", false, new Product[20]);
        testUsers[2] = new User(101,"Sally", false, new Product[20]);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the User array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),User[].class))
                .thenReturn(testUsers);
        UserFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetUsers() {
        // Invoke
        User[] users = UserFileDAO.getUsers();

        // Analyze
        assertEquals(users.length,testUsers.length);
        for (int i = 0; i < testUsers.length;++i)
            assertEquals(users[i],testUsers[i]);
    }

    @Test
    public void testFindUsers() {
        // Invoke
        User[] heroes = UserFileDAO.findUsers("Jo");

        // Analyze
        assertEquals(heroes.length,2);
        assertEquals(heroes[0],testUsers[0]);
        assertEquals(heroes[1],testUsers[1]);
    }

    @Test
    public void testGetUser() {
        // Invoke
        User user = UserFileDAO.getUser(99);

        // Analzye
        assertEquals(user,testUsers[0]);
    }

    @Test
    public void testDeleteUser() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> UserFileDAO.deleteUser(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test Users array - 1 (because of the delete)
        // Because Users attribute of HeroFileDAO is package private
        // we can access it directly
        assertEquals(UserFileDAO.users.size(),testUsers.length-1);//Should users be public
    }

    @Test
    public void testCreateUser() throws IOException {
        // Setup
        User user = new User(102, "Dude", false, new Product[20]);

        // Invoke
        User result = assertDoesNotThrow(() -> UserFileDAO.createUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        assertEquals(result.getId(),user.getId());
        assertEquals(result.getUsername(),user.getUsername());
    }

    @Test
    public void testCreateUserFail() throws IOException {
        // Setup
        User user = new User(101, "Dude", false, new Product[20]);
        UserFileDAO.createUser(user);

        // Invoke
        User result = assertDoesNotThrow(() -> UserFileDAO.createUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testUpdateUser() throws IOException {
        // Setup
        User user = new User(101,"Dude", false, new Product[20]);


        // Invoke
        User result = assertDoesNotThrow(() -> UserFileDAO.updateUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = UserFileDAO.getUser(user.getId());
        assertEquals(actual,user);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));

        User user = new User(102,"Dude", false, new Product[20]);

        assertThrows(IOException.class,
                        () -> UserFileDAO.createUser(user),
                        "IOException not thrown");
    }

    @Test
    public void testGetUserNotFound() {
        // Invoke
        User hero = UserFileDAO.getUser(98);

        // Analyze
        assertEquals(hero,null);
    }

    @Test
    public void testDeleteUserNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> UserFileDAO.deleteUser(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(UserFileDAO.users.size(),testUsers.length);
    }

    @Test
    public void testDeleteUser0() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> UserFileDAO.deleteUser(0),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(UserFileDAO.users.size(),testUsers.length);
    }

    @Test
    public void testUpdateUserNotFound() throws IOException {
        // Setup
        User hero = new User(102, "Dude", false, new Product[20]);

        // Invoke
        User result = assertDoesNotThrow(() -> UserFileDAO.updateUser(hero),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the UserFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),User[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new UserFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
