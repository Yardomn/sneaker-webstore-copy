package com.estore.api.estoreapi.model;

//necessary imports

import java.util.logging.Logger;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.*;


public class ShoppingCart {
    
    private HashMap< Product, Integer > shoppingCart; // shoppingCart is the hasmap of products related to its amount

    @JsonProperty("totalCost")
    private int totalCost;

    @JsonProperty("isCompleteOrder")
    private boolean isCompleteOrder;


    @JsonCreator
    public ShoppingCart(){
        this.isCompleteOrder = false;
        this.shoppingCart = new HashMap<>();
        this.totalCost = 0;
    }

    @JsonIgnore
    Product [] updatedProducts;

    @JsonIgnore
    private static final Logger LOG = Logger.getLogger(ShoppingCart.class.getName());

    public HashMap<Product, Integer> getShoppingcart(){
        return shoppingCart;
    }

    public Product[] getProducts(){
        return updatedProducts;
    }


    public boolean getisOrderComplete(){
        return isCompleteOrder;
    }

    public int getTotalCost(){
        return this.totalCost;
    }

    public void setOrderComplete(boolean b){
        this.isCompleteOrder = b;
    }

    public void setTotalCost(int cost){
        this.totalCost =  cost;
    }


    @JsonIgnore
    public void setFinalCost(){
        this.totalCost = 0;
        for(HashMap.Entry<Product, Integer> entry: shoppingCart.entrySet()){
            Product p = entry.getKey();
            int quantity = entry.getValue();
            float price = p.getPrice();
            this.totalCost += (price * quantity);
        }
        setTotalCost(this.totalCost);
    }

    @JsonIgnore
    public boolean addToCart(Product product){
        if (product == null){
            LOG.info("ShoppingCart - addToCart() failed. Product not in existence");
            return false;
        }
        else if (shoppingCart.containsKey(product)){
            int quantity = shoppingCart.get(product)+1;
            shoppingCart.put(product, quantity);
            setFinalCost();
            return true;
        }
        else {
            if (this.isCompleteOrder == true){
                this.isCompleteOrder=false;
            }
            shoppingCart.put(product,1);
            setFinalCost();
            return true;
        }
    }

    @JsonIgnore
    public boolean removeFromCart(Product product) {
        if (product == null) {
            LOG.info("ShoppingCart - removeFromCart() failed. Product DNE");
            return false;
        } else if (shoppingCart.containsKey(product)) {
            if (shoppingCart.get(product) == 1) {
                shoppingCart.remove(product);
            } else {
                int newQuantity = shoppingCart.get(product) - 1;
                shoppingCart.put(product, newQuantity);

            }
            setFinalCost();
            return true;
        } else {
            LOG.info("ShoppingCart - removeFromCart() failed. cart does not contain this product.");
            return false;
        }
    }
    
    @JsonIgnore
    public boolean getupdatedProducts() {
        this.updatedProducts = new Product[shoppingCart.keySet().toArray().length];
        int newQuantity;
        Product newProduct;
        boolean validPurchase = true;
        int i = 0;
        if(shoppingCart.entrySet().size() == 0){
            LOG.info("ShoppingCart - getUpdatedProducts() failed. Empty Cart");
            validPurchase = false;
            return validPurchase;
        }
        for (HashMap.Entry<Product, Integer> entry : shoppingCart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            if (product.getQuantity() >= quantity) {
                newQuantity = product.getQuantity() - quantity;
                newProduct = new Product(product.getId(), product.getName(), product.getPrice(), newQuantity, product.getDescription());
                updatedProducts[i++] = newProduct;
            } else {
                LOG.info("ShoppingCart - getUpdatedProducts() failed. Invalid purchase, update quantity amount of " + product.getName());
                validPurchase = false;
                break;
            }
        }
        return validPurchase;
    }

    public boolean completePurchase() {
        if(getupdatedProducts()){
            this.shoppingCart.clear();
            setOrderComplete(true);
            return true;
        }
        else{
           return false;
        }
    }

    @Override
    public String toString() {
        return "ShoppingCart (isOrderComplete=" + isCompleteOrder + ", shoppingCart=" + shoppingCart + ", totalCost="
                + totalCost + ")";
    }

    
    
}
