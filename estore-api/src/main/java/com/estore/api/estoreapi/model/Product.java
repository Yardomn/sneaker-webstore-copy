package com.estore.api.estoreapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Product entity
 *
 * @author SWEN Faculty
 * @author team0
 */
public class Product {
    private static final Logger LOG = Logger.getLogger(Product.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Product [id=%d, name=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("price") private float price;
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("description") private String description;



    /**
     * Create a product with the given id and name
     * @param id The id of the product
     * @param name The name of the product
     *
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("price") float price, @JsonProperty("quantity")  int quantity, @JsonProperty("description") String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
    }


    /**
     * Retrieves the id of the product
     * @return The id of the product
     */
    public int getId() {return id;}

    /**
     * Sets the name of the product - necessary for JSON object to Java object deserialization
     * @param name The name of the product
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the product
     * @return The name of the product
     */
    public String getName() {return name;}

    /**
     * Sets the price of the product - necessary for JSON object to Java object deserialization
     * @param price The price of the product
     */
    public void setPrice(float price) {this.price = price;}

    /**
     * Retrieves the price of the product
     * @return The price of the product
     */
    public float getPrice() {return price;}

    /**
     * Sets the quantity of the product - necessary for JSON object to Java object deserialization
     * @param quantity The quanitty of the product
     */
    public void setQuantity(int quantity) {this.quantity = quantity;}

    /**
     * Retrieves the quantity of the product
     * @return The quantity of the product
     */
    public int getQuantity() {return quantity;}

     /**
     * Sets the quantity of the product - necessary for JSON object to Java object deserialization
     * @param description The quanitty of the product
     */
    public void setDescription(String description) {this.description = description;}

    /**
     * Retrieves the quantity of the product
     * @return The quantity of the product
     */
    public String getDescription() {return description;}

    

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name);
    }
}
