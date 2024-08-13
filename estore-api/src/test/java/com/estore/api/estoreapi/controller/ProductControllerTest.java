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

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistance.ProductDAO;

/**
 * Test the Product Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class ProductControllerTest {
    private ProductController productController;
    private ProductDAO mockProductDAO;

    /**
     * Before each test, create a new ProductController object and inject
     * a mock Product DAO
     */
    @BeforeEach
    public void setupProductController() {
        mockProductDAO = mock(ProductDAO.class);
        productController = new ProductController(mockProductDAO);
    }

    @Test
    public void testGetProduct() throws IOException {  // getProduct may throw IOException
        // Setup
        Product hero = new Product(99,"Galactic Agent",90,10,"yes");
        // When the same id is passed in, our mock Product DAO will return the Product object
        when(mockProductDAO.getProduct(hero.getId())).thenReturn(hero);

        // Invoke
        ResponseEntity<Product> response = productController.getProduct(hero.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(hero,response.getBody());
    }

    @Test
    public void testGetProductNotFound() throws Exception { // createProduct may throw IOException
        // Setup
        int heroId = 99;
        // When the same id is passed in, our mock Product DAO will return null, simulating
        // no product found
        when(mockProductDAO.getProduct(heroId)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = productController.getProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetProductHandleException() throws Exception { // createProduct may throw IOException
        // Setup
        int heroId = 99;
        // When getProduct is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).getProduct(heroId);

        // Invoke
        ResponseEntity<Product> response = productController.getProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all ProductController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateProduct() throws IOException {  // createProduct may throw IOException
        // Setup
        Product product = new Product(99," Agent",90,10,"yes");
        // when createProduct is called, return true simulating successful
        // creation and save
        when(mockProductDAO.createProduct(product)).thenReturn(product);

        // Invoke
        ResponseEntity<Product> response = productController.createProduct(product);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(product,response.getBody());
    }

    @Test
    public void testCreateProductFailed() throws IOException {  // createProduct may throw IOException
        // Setup
        Product product = new Product(99,"Bolt",90,10,"yes");
        // when createProduct is called, return false simulating failed
        // creation and save
        when(mockProductDAO.createProduct(product)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = productController.createProduct(product);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateProductHandleException() throws IOException {  // createProduct may throw IOException
        // Setup
        Product product = new Product(99,"Galactic Agent",90,10,"yes");
        // When createProduct is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).createProduct(product);

        // Invoke
        ResponseEntity<Product> response = productController.createProduct(product);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateProduct() throws IOException { // updateProduct may throw IOException
        // Setup
        Product product = new Product(99,"Galactic Agent",90,10,"yes");
        // when updateProduct is called, return true simulating successful
        // update and save
        when(mockProductDAO.updateProduct(product)).thenReturn(product);
        ResponseEntity<Product> response = productController.updateProduct(product);
        product.setName("Bolt");

        // Invoke
        response = productController.updateProduct(product);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(product,response.getBody());
    }

    @Test
    public void testUpdateProductFailed() throws IOException { // updateProduct may throw IOException
        // Setup
        Product product = new Product(99,"Galactic Agent",90,10,"yes");
        // when updateProduct is called, return true simulating successful
        // update and save
        when(mockProductDAO.updateProduct(product)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = productController.updateProduct(product);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateProductHandleException() throws IOException { // updateProduct may throw IOException
        // Setup
        Product product = new Product(99,"Galactic Agent",90,10,"yes");
        // When updateProduct is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).updateProduct(product);

        // Invoke
        ResponseEntity<Product> response = productController.updateProduct(product);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetProducts() throws IOException { // getProducts may throw IOException
        // Setup
        Product[] products = new Product[2];
        products[0] = new Product(99,"Bolt",90,10,"yes");
        products[1] = new Product(100,"Galactic Agent",90,10,"yes");
        // When getProducts is called return the heroes created above
        when(mockProductDAO.getProducts()).thenReturn(products);

        // Invoke
        ResponseEntity<Product[]> response = productController.getProducts();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(products,response.getBody());
    }
    
    @Test
    public void getProducts_ReturnsNotFound_WhenNoProductsAreFound() throws IOException {
        when(mockProductDAO.getProducts()).thenReturn(null);

        ResponseEntity<Product[]> response = productController.getProducts();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetProductsHandleException() throws IOException { // getProducts may throw IOException
        // Setup
        // When getProducts is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).getProducts();

        // Invoke
        ResponseEntity<Product[]> response = productController.getProducts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchProducts() throws IOException { // findProducts may throw IOException
        // Setup
        String searchString = "la";
        Product[] products = new Product[2];
        products[0] = new Product(99,"Bolt",90,10,"yes");
        products[1]= new Product(100,"Galactic Agent",90,10,"yes");
        // When findProducts is called with the search string, return the two
        /// products above
        when(mockProductDAO.findProducts(searchString)).thenReturn(products);

        // Invoke
        ResponseEntity<Product[]> response = productController.searchProducts(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(products,response.getBody());
    }

    @Test
    public void testSearchProductsHandleException() throws IOException { // findProducts may throw IOException
        // Setup
        String searchString = "an";
        // When createProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).findProducts(searchString);

        // Invoke
        ResponseEntity<Product[]> response = productController.searchProducts(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteProduct() throws IOException { // deleteProducts may throw IOException
        // Setup
        int heroId = 99;
        // when deleteProduct is called return true, simulating successful deletion
        when(mockProductDAO.deleteProduct(heroId)).thenReturn(true);

        // Invoke
        ResponseEntity<Product> response = productController.deleteProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteProductNotFound() throws IOException { // deleteProduct may throw IOException
        // Setup
        int heroId = 99;
        // when deleteProduct is called return false, simulating failed deletion
        when(mockProductDAO.deleteProduct(heroId)).thenReturn(false);

        // Invoke
        ResponseEntity<Product> response = productController.deleteProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteProductHandleException() throws IOException { // deleteProduct may throw IOException
        // Setup
        int heroId = 99;
        // When deleteProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).deleteProduct(heroId);

        // Invoke
        ResponseEntity<Product> response = productController.deleteProduct(heroId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
