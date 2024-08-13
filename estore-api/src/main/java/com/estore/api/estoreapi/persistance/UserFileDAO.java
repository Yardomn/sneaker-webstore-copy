package com.estore.api.estoreapi.persistance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;


import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.estore.api.estoreapi.model.User;

/**
 * Implements the functionality for JSON file-based persistance for Users
 *
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 *
 * @author team0
 */
@Component
public class UserFileDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());
    public Map<Integer,User> users;   // Provides a local cache of the User objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between User
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;
    private String filename;    // Filename to read from and write to

    /**
     * Creates a User File Data Access Object
     *
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     *
     * @throws IOException when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the users from the file
    }

    /**
     * Generates an array of {@linkplain User Users} from the tree map
     *
     * @return  The array of {@link User Users}, may be empty
     */
    private User[] getUsersArray() {
        return getUsersArray(null);
    }

    /**
     * Makes the next id for a new {@linkplain User user}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    

    /**
     * Generates an array of {@linkplain User Users} from the tree map for any
     * {@linkplain User Users} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain User Users}
     * in the tree map
     *
     * @return  The array of {@link User Users}, may be empty
     */
    private User[] getUsersArray(String containsText) { // if containsText == null, no filter
        ArrayList<User> userArrayList = new ArrayList<>();

        for (User user : users.values()) {
            if (containsText == null || user.getUsername().contains(containsText)) {
                userArrayList.add(user);
            }
        }
        User[] userArray = new User[userArrayList.size()];
        userArrayList.toArray(userArray);
        return userArray;
    }

    /**
     * Saves the {@linkplain User Users} from the map into the file as an array of JSON objects
     *
     * @return true if the {@link User Users} were written successfully
     *
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        User[] UserArray = getUsersArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),UserArray);
        return true;
    }

    /**
     * Loads {@linkplain User Users} from the JSON file into the map
     * <br>
     *
     * @return true if the file was read successfully
     *
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        users = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of Users
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        User[] UserArray = objectMapper.readValue(new File(filename),User[].class);

        // Add each User to the tree map
        for (User user : UserArray) {
            users.put(user.getId(),user);
            if (user.getId() > nextId)
                nextId = user.getId();
        }
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] getUsers() {
        synchronized(users) {
            return getUsersArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User[] findUsers(String containsText) {
        synchronized(users) {
            return getUsersArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public User getUser(int ID) {
        synchronized(users) {
            if (users.containsKey(ID))
                return users.get(ID);
            else
                return null;
        }
    }

    @Override
    /**
     ** {@inheritDoc}
     */
    public User createUser(User user) throws IOException {
        synchronized(users) {
            if (getUsersArray(user.getUsername()).length == 0) {
                User newUser = new User (nextId(),user.getUsername(), user.getIsLoggedIn(), user.getCart());
                users.put(newUser.getId(), newUser);
                save(); // may throw an IOException
                return newUser; 
            }
            else {
                return null;
            }
        }
    }

    @Override
    /**
     ** {@inheritDoc}
     */
    public User updateUser(User user) throws IOException {
        synchronized(user) {
            if (users.containsKey(user.getId()) == false){
                return null;  // user does not exist
            }else{
                users.put(user.getId(),user);
                save(); // may throw an IOException
                return user;
            }
        }
    }
    
    @Override
    /**
    ** {@inheritDoc}
    */
    public boolean deleteUser(int id) throws IOException {
        synchronized(users) {
            if (id > 0 && users.containsKey(id)) {
                users.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}