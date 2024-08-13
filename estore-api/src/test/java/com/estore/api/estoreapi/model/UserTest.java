package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Product class
 * 
 * @author rcb4416
 */
@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCtor() throws IOException {
        //Setup
        int expected_id = 99;
        String expected_usrnm = "Joe";
        Boolean expected_log = false;
        Product[] expected_cart = new Product[20];

        //Invoke
        User user = new User(expected_id, expected_usrnm, expected_log, expected_cart);

        //Analyze
        assertEquals(expected_id, user.getId());
        assertEquals(expected_usrnm, user.getUsername());
        assertEquals(expected_log, user.getIsLoggedIn());
        assertEquals(expected_cart, user.getCart());
    }

    @Test
    public void testSetter() throws IOException {
        //Setup
        int id = 99;
        String usrnm = "Joe";
        Boolean log = false;
        Product[] cart = new Product[20];
        User user = new User(id, usrnm, log, cart);

        String expected_name = "Joe_mom";
        boolean expected_log = true;

        // Invoke
        user.setUserName(expected_name);
        user.setIsLoggedIn(expected_log);


        // Analyze
        assertEquals(expected_name,user.getUsername());
        assertEquals(expected_log,user.getIsLoggedIn());
        assertTrue(!user.isUserAdmin(user));
    }


    @Test
    public void testUnloggedAdmin() throws IOException {
        //Setup
        int id = 99;
        String usrnm = "admin";
        Boolean log = false;
        Product[] cart = new Product[20];
        User user = new User(id, usrnm, log, cart);
        Product p = new Product(99, "Sneaker", 10, 12, "cool beans");

        //Invoke
        boolean is_admin = user.isUserAdmin(user);

        //Analyze
        assertTrue(is_admin);
        assertNull(user.getShoppingCart());
        assertTrue(!user.addToCart(p));
        assertTrue(!user.removeFromCart(p));
        assertTrue(!user.completeOrder());
    }

    @Test
    public void testLoggedAdmin() throws IOException {
        //Setup
        int id = 99;
        String usrnm = "admin";
        Boolean log = true;
        Product[] cart = new Product[20];
        User user = new User(id, usrnm, log, cart);
        Product p = new Product(99, "Sneaker", 10, 12, "cool beans");

        //Invoke
        boolean is_admin = user.isUserAdmin(user);

        //Analyze
        assertTrue(is_admin);
        assertNull(user.getShoppingCart());
        assertTrue(!user.addToCart(p));
        assertTrue(!user.removeFromCart(p));
        assertTrue(!user.completeOrder());
    }

    @Test
    public void testUnloggedUser() throws IOException {
        //Setup
        int id = 99;
        String usrnm = "Joe";
        Boolean log = false;
        Product[] cart = new Product[20];
        User user = new User(id, usrnm, log, cart);
        Product p = new Product(99, "Sneaker", 10, 12, "cool beans");

        //Invoke
        boolean added = user.addToCart(p);
        boolean removed = user.removeFromCart(p);
        boolean complete = user.completeOrder();
        ShoppingCart actualCart = user.getShoppingCart();


        //Analyze
        assertEquals(0, actualCart.getTotalCost());
        assertTrue(!added);
        assertTrue(!removed);
        assertTrue(!complete);
    }

    @Test
    public void testLoggedUser() throws IOException {
        //Setup
        int id = 99;
        String usrnm = "Joe";
        Boolean log = true;
        Product[] cart = new Product[20];
        User user = new User(id, usrnm, log, cart);
        Product p = new Product(99, "Sneaker", 10, 12, "cool beans");

        //Invoke
        boolean complete_fail = user.completeOrder();
        boolean added = user.addToCart(p);
        boolean complete = user.completeOrder();
        ShoppingCart actualCart = user.getShoppingCart();


        //Analyze
        assertEquals(10, actualCart.getTotalCost());
        assertTrue(!complete_fail);
        assertTrue(added);
        assertTrue(complete);
    }

    @Test
    public void testLoggedUserRemove() throws IOException {
        //Setup
        int id = 99;
        String usrnm = "Joe";
        Boolean log = true;
        Product[] cart = new Product[20];
        User user = new User(id, usrnm, log, cart);
        Product p = new Product(99, "Sneaker", 10, 12, "cool beans");

        //Invoke
        boolean complete_fail = user.completeOrder();
        boolean added = user.addToCart(p);
        boolean removed = user.removeFromCart(p);
        ShoppingCart actualCart = user.getShoppingCart();


        //Analyze
        assertEquals(0, actualCart.getTotalCost());
        assertTrue(!complete_fail);
        assertTrue(added);
        assertTrue(removed);
    }

    @Test
    public void testEquals() throws IOException {
        //Setup
        int id = 99;
        String usrnm = "Joe";
        Boolean log = false;
        Product[] cart = new Product[20];
        User user = new User(id, usrnm, log, cart);
        id = 100;
        User otheruser1 = new User(id, usrnm, log, cart);
        usrnm = "Mom";
        User otheruser2 = new User(id, usrnm, log, cart);
        id = 99;
        User otheruser3 = new User(id, usrnm, log, cart);

        //Invoke
        boolean diff_id = user.equals(otheruser1);
        boolean diff_id_name = user.equals(otheruser2);
        boolean diff_name = user.equals(otheruser3);
        boolean not_user = user.equals(usrnm);

        //Analyze
        assertTrue(!diff_id);
        assertTrue(!diff_id_name);
        assertTrue(!diff_name);
        assertTrue(!not_user);

    }


}