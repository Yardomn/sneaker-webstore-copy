package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistance.UserDAO;
import com.estore.api.estoreapi.model.Product;


/**
 * Test the User Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private static final boolean True = false;
    private UserController UserController;
    private UserDAO mockUserDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        UserController = new UserController(mockUserDAO);
    }

    @Test
    public void testGetUser() throws IOException {  // getUser may throw IOException
        // Setup
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", true, Cart);
        // When the same id is passed in, our mock User DAO will return the User object
        when(mockUserDAO.getUser(joe.getId())).thenReturn(joe);

        // Invoke
        ResponseEntity<User> response = UserController.getUser(joe.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(joe,response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws Exception { // createUser may throw IOException
        // Setup
        int joeId = 99;
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no User found
        when(mockUserDAO.getUser(joeId)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = UserController.getUser(joeId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception { // createUser may throw IOException
        // Setup
        int joeId = 99;
        // When getUser is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(joeId);

        // Invoke
        ResponseEntity<User> response = UserController.getUser(joeId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all UserController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateUser() throws IOException {  // createUser may throw IOException
        // Setup
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", True, Cart);
        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.createUser(joe)).thenReturn(joe);

        // Invoke
        ResponseEntity<User> response = UserController.createUser(joe);
        //since users is empty right now the contains statement in createUser is throwing a null pointer exception

        // Analyze
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
        assertEquals(joe,response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException {  // createUser may throw IOException
        // Setup
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", True, Cart);
        User joe1 = new User(99, "Joe", True, Cart);
        // when createUser is called, return false simulating failed
        // creation and save
        mockUserDAO.createUser(joe);
        UserController.createUser(joe);
        when(mockUserDAO.createUser(joe1)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = UserController.createUser(joe1);
        System.out.print(mockUserDAO.getUsers());
        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException {  // createUser may throw IOException
        // Setup
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", True, Cart);
        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).createUser(joe);

        // Invoke
        ResponseEntity<User> response = UserController.createUser(joe);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException { // updateUser may throw IOException
        // Setup
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", True, Cart);
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(joe)).thenReturn(joe);
        ResponseEntity<User> response = UserController.updateUser(joe);
        joe.setUserName("Bolt");

        // Invoke
        response = UserController.updateUser(joe);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(joe,response.getBody());
    }

    @Test
    public void testUpdateUserFailed() throws IOException { // updateUser may throw IOException
        // Setup
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", True, Cart);
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(joe)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = UserController.updateUser(joe);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleException() throws IOException { // updateUser may throw IOException
        // Setup
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", True, Cart);
        // When updateUser is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).updateUser(joe);

        // Invoke
        ResponseEntity<User> response = UserController.updateUser(joe);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException { // getUsers may throw IOException
        // Setup
        User[] Users = new User[2];
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", True, Cart);
        // When getUsers is called return the heroes created above
        when(mockUserDAO.getUsers()).thenReturn(Users);

        // Invoke
        ResponseEntity<User[]> response = UserController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(Users,response.getBody());
    }
    
    @Test
    public void getUsers_ReturnsNotFound_WhenNoUsersAreFound() throws IOException {
        when(mockUserDAO.getUsers()).thenReturn(null);

        ResponseEntity<User[]> response = UserController.getUsers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUsersHandleException() throws IOException { // getUsers may throw IOException
        // Setup
        // When getUsers is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = UserController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchUsers() throws IOException { // findUsers may throw IOException
        // Setup
        String searchString = "la";
        User[] Users = new User[2];
        Product[] Cart = new Product[20];
        User joe = new User(99, "Joe", True, Cart);
        // When findUsers is called with the search string, return the two
        /// Users above
        when(mockUserDAO.findUsers(searchString)).thenReturn(Users);

        // Invoke
        ResponseEntity<User[]> response = UserController.searchUsers(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(Users,response.getBody());
    }

    @Test
    public void testSearchUsersHandleException() throws IOException { // findUsers may throw IOException
        // Setup
        String searchString = "an";
        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).findUsers(searchString);

        // Invoke
        ResponseEntity<User[]> response = UserController.searchUsers(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteUser() throws IOException { // deleteUsers may throw IOException
        // Setup
        int joeId = 99;
        // when deleteUser is called return true, simulating successful deletion
        when(mockUserDAO.deleteUser(joeId)).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = UserController.deleteUser(joeId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException { // deleteUser may throw IOException
        // Setup
        int joeId = 99;
        // when deleteUser is called return false, simulating failed deletion
        when(mockUserDAO.deleteUser(joeId)).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = UserController.deleteUser(joeId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException { // deleteUser may throw IOException
        // Setup
        int joeId = 99;
        // When deleteUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).deleteUser(joeId);

        // Invoke
        ResponseEntity<User> response = UserController.deleteUser(joeId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
