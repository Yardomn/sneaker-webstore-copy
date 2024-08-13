package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Shopping Cart class
 * 
 * @author Raymond Babich
 */
@Tag("Model-tier")
public class ShoppingCartTest{
    @Test
    public void testCtor() {
        //Setup
        HashMap< Product, Integer > expectedCart = new HashMap<>();

        // Invoke
        ShoppingCart cart = new ShoppingCart();

        //Analyze
        assertEquals(false, cart.getisOrderComplete());
        assertTrue(expectedCart.equals(cart.getShoppingcart()));
        assertEquals(0, cart.getTotalCost());
    }

    @Test
    public void testAddPass(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();
        HashMap< Product, Integer > expectedCart = new HashMap<>();
        expectedCart.put(product, 1);

        //Invoke
        boolean isadded = cart.addToCart(product);

        //Analyze
        assertTrue(isadded);
        assertEquals(9, cart.getTotalCost());
        assertTrue(expectedCart.equals(cart.getShoppingcart()));
    }

    @Test
    public void testAddComplete(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();
        HashMap< Product, Integer > expectedCart = new HashMap<>();
        expectedCart.put(product, 1);
        cart.addToCart(product);
        cart.completePurchase();

        //Invoke
        boolean isadded = cart.addToCart(product);

        //Analyze
        assertTrue(isadded);
        assertEquals(9, cart.getTotalCost());
        assertTrue(expectedCart.equals(cart.getShoppingcart()));
    }

    @Test
    public void testAddFail(){
        //Setup
        ShoppingCart cart = new ShoppingCart();
        HashMap< Product, Integer > expectedCart = new HashMap<>();

        //Invoke
        boolean isadded = cart.addToCart(null);

        //Analyze
        assertTrue(!isadded);
        assertEquals(0, cart.getTotalCost());
        assertTrue(expectedCart.equals(cart.getShoppingcart()));
    }

    @Test
    public void testRemovePass1(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();
        HashMap< Product, Integer > expectedCart = new HashMap<>();

        //Invoke
        cart.addToCart(product);
        boolean isremoved = cart.removeFromCart(product);

        //Analyze
        assertTrue(isremoved);
        assertEquals(0, cart.getTotalCost());
        assertTrue(expectedCart.equals(cart.getShoppingcart()));
    }

    @Test
    public void testRemovePass2(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();
        HashMap< Product, Integer > expectedCart = new HashMap<>();
        expectedCart.put(product, 1);

        //Invoke
        cart.addToCart(product);
        cart.addToCart(product);
        boolean isremoved = cart.removeFromCart(product);

        //Analyze
        assertTrue(isremoved);
        assertEquals(9, cart.getTotalCost());
        assertTrue(expectedCart.equals(cart.getShoppingcart()));
    }

    @Test
    public void testRemoveFail1(){
        //Setup
        ShoppingCart cart = new ShoppingCart();
        HashMap< Product, Integer > expectedCart = new HashMap<>();

        //Invoke
        boolean isremoved = cart.removeFromCart(null);

        //Analyze
        assertTrue(!isremoved);
        assertEquals(0, cart.getTotalCost());
        assertTrue(expectedCart.equals(cart.getShoppingcart()));
    }

    @Test
    public void testRemoveFail2(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();
        HashMap< Product, Integer > expectedCart = new HashMap<>();

        //Invoke
        boolean isremoved = cart.removeFromCart(product);

        //Analyze
        assertTrue(!isremoved);
        assertEquals(0, cart.getTotalCost());
        assertTrue(expectedCart.equals(cart.getShoppingcart()));
    }

    @Test
    public void testompletePass(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();

        //Invoke
        cart.addToCart(product);
        boolean iscomplete = cart.completePurchase();
        

        //Analyze
        assertTrue(iscomplete);
    }

    @Test
    public void testCompleteFail1(){
        //Setup
        ShoppingCart cart = new ShoppingCart();
        HashMap< Product, Integer > expectedCart = new HashMap<>();

        //Invoke
        boolean iscomplete = cart.completePurchase();

        //Analyze
        assertTrue(!iscomplete);
    }

    @Test
    public void testCompleteFail2(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 0;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();

        //Invoke
        cart.addToCart(product);
        boolean iscomplete = cart.completePurchase();
        

        //Analyze
        assertTrue(!iscomplete);
    }

    @Test
    public void testToString(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 0;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();
        String expected = "ShoppingCart (isOrderComplete=false, shoppingCart={Product [id=99, name=Wi-Fire]=1}, totalCost=9)";

        //Invoke
        cart.addToCart(product);
        String actual = cart.toString();

        //Analyze
        System.out.println(actual);
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGetProducts(){
        //Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);
        ShoppingCart cart = new ShoppingCart();

        //Invoke
        cart.addToCart(product);
        cart.completePurchase();
        Product[] actual_updt_cart = cart.getProducts();

        //Analyze
        assertNotNull(actual_updt_cart);
    }
}