package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Product class
 * 
 * @author SWEN Faculty
 */
@Tag("Model-tier")
public class ProductTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "Wi-Fire";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";

        // Invoke
        Product product = new Product(expected_id,expected_name,expected_price,expected_quantity,expected_description);

        // Analyze
        assertEquals(expected_id,product.getId());
        assertEquals(expected_name,product.getName());
        assertEquals(expected_price,product.getPrice());
        assertEquals(expected_quantity,product.getQuantity());
        assertEquals(expected_description,product.getDescription());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        float price = 9;
        int quanitity = 10;
        String description = "no";
        Product product = new Product(id,name,price,quanitity,description);

        String expected_name = "Galactic Agent";
        float expected_price = 9;
        int expected_quantity = 10;
        String expected_description = "yes";

        // Invoke
        product.setName(expected_name);
        product.setPrice(expected_price);
        product.setQuantity(expected_quantity);
        product.setDescription(expected_description);

        // Analyze
        assertEquals(expected_name,product.getName());
        assertEquals(expected_price,product.getPrice());
        assertEquals(expected_quantity,product.getQuantity());
        assertEquals(expected_description,product.getDescription());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String name = "Wi-Fire";
        String expected_string = String.format(Product.STRING_FORMAT,id,name);
        Product hero = new Product(id,name,9,10,"yes");

        // Invoke
        String actual_string = hero.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}