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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Product File DAO class
 * 
 * @author SWEN Faculty
 */
@Tag("Persistence-tier")
public class ProductFileDAOTest {
    ProductFileDAO ProductFileDAO;
    Product[] testProducts;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupProductFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testProducts = new Product[3];
        testProducts[0] = new Product(0,"Wi-Fire",9,10,"no");
        testProducts[1] = new Product(1,"Galactic Agent",9,10,"no");
        testProducts[2] = new Product(2,"Ice Gladiator",9,10,"no");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the product array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Product[].class))
                .thenReturn(testProducts);
        ProductFileDAO = new ProductFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetProducts() {
        // Invoke
        Product[] heroes = ProductFileDAO.getProducts();

        // Analyze
        assertEquals(heroes.length,testProducts.length);
        for (int i = 0; i < testProducts.length;++i)
            assertEquals(heroes[i],testProducts[i]);
    }

    @Test
    public void testFindProducts() {
        // Invoke
        Product[] heroes = ProductFileDAO.findProducts("la");

        // Analyze
        assertEquals(heroes.length,2);
        assertEquals(heroes[0],testProducts[1]);
        assertEquals(heroes[1],testProducts[2]);
    }

    @Test
    public void testGetProduct() {
        // Invoke
        Product hero = ProductFileDAO.getProduct(0);

        // Analzye
        assertEquals(hero,testProducts[0]);
    }

    @Test
    public void testDeleteProduct() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> ProductFileDAO.deleteProduct(0),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test products array - 1 (because of the delete)
        // Because products attribute of HeroFileDAO is package private
        // we can access it directly
        assertEquals(ProductFileDAO.products.size(),testProducts.length-1);
    }

    @Test
    public void testCreateProduct() {
        // Setup
        Product product = new Product(3,"Wonder-Person",9,10,"no");

        // Invoke
        Product result = assertDoesNotThrow(() -> ProductFileDAO.createProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = ProductFileDAO.getProduct(product.getId());
        assertEquals(actual.getId(),product.getId());
        assertEquals(actual.getName(),product.getName());
    }


    @Test
    public void testUpdateProduct() {
        // Setup
        Product product = new Product(0,"Galactic Agent",9,10,"no");

        // Invoke
        Product result = assertDoesNotThrow(() -> ProductFileDAO.updateProduct(product),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Product actual = ProductFileDAO.getProduct(product.getId());
        assertEquals(actual,product);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Product[].class));

        Product hero = new Product(3,"Wi-Fire",9,10,"no");

        assertThrows(IOException.class,
                        () -> ProductFileDAO.createProduct(hero),
                        "IOException not thrown");
    }

    @Test
    public void testGetProductNotFound() {
        // Invoke
        Product hero = ProductFileDAO.getProduct(98);

        // Analyze
        assertEquals(hero,null);
    }

    @Test
    public void testDeleteProductNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> ProductFileDAO.deleteProduct(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(ProductFileDAO.products.size(),testProducts.length);
    }

    @Test
    public void testUpdateProductNotFound() {
        // Setup
        Product hero = new Product(98,"Bolt",9,10,"no");

        // Invoke
        Product result = assertDoesNotThrow(() -> ProductFileDAO.updateProduct(hero),
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
        // from the ProductFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Product[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new ProductFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}
