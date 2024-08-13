package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;

import com.estore.api.estoreapi.persistance.UserDAO;
import com.estore.api.estoreapi.persistance.ProductDAO;
import com.estore.api.estoreapi.model.User;

/**
 * Handles the REST API requests for the User resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 *
 * @author team0
 */

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDao;
    private ProductDAO productDao;

    /**
     * Creates a REST API controller to reponds to requests
     *
     * @param userDao The {@link UserDAO user Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public UserController(UserDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * Responds to the GET request for a {@linkplain User User} for the given id
     *
     * @param id The id used to locate the {@link User User}
     *
     * @return ResponseEntity with {@link User User} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        LOG.info("GET /users/" +  id);
        try {
            User user = userDao.getUser(id);
            if (user != null){
                return new ResponseEntity<User>(user,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain User User}
     *
     * @return ResponseEntity with array of {@link User User} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     *
     */
    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users");
        try {
            User[] user = userDao.getUsers();
            if(user != null){
                return new ResponseEntity<User[]>(user,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain User User} whose name contains
     * the text in name
     *
     * @param name The name parameter which contains the text used to find the {@link User User}
     *
     * @return ResponseEntity with array of {@link User User} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all estore that contain the text "ma"
     * GET http://localhost:8080/estore/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<User[]> searchUsers(@RequestParam String username) {
        LOG.info("GET /users/?username="+username);
        try {
            User[] user = userDao.findUsers(username);
            return new ResponseEntity<User[]>(user,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain User User} with the provided User object
     *
     * @param User - The {@link User User} to create
     *
     * @return ResponseEntity with created {@link User User} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link User User} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /users " + user);

        try{
            User y = userDao.createUser(user);
            
            if (y == null){
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            else{
                return new ResponseEntity<User>(y, HttpStatus.ACCEPTED);
            }
        }
        catch (IOException err){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain User User} with the provided {@linkplain User User} object, if it exists
     *
     * @param User The {@link User User} to update
     *
     * @return ResponseEntity with updated {@link User User} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        LOG.info("PUT /users " + user);
        try {
            User updatedUser = userDao.updateUser(user);

            if (updatedUser != null) {
                return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain User User} with the given id
     *
     * @param id The id of the {@link User User} to deleted
     *
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        LOG.info("DELETE /users/" + id);

        try{
            boolean y = userDao.deleteUser(id);
            if (y == true){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }   
        }
        catch(IOException err){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}