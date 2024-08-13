package com.estore.api.estoreapi.model;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;

public class User {
    

    @JsonProperty("id")
    private int id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("isLoggedIn")
    private boolean isLoggedIn;
    @JsonProperty("cart")
    private Product[] cart;

    @JsonIgnore
    private ShoppingCart shoppingCart;
    @JsonIgnore
    private static final Logger LOG = Logger.getLogger(User.class.getName());

    public User( @JsonProperty("id") int id,  @JsonProperty("username") String username,  @JsonProperty("isLoggedIn") boolean isLoggedIn
    , @JsonProperty("cart") Product[] cart ) throws JsonProcessingException{

        this.id = id;
        this.username = username;
        this.isLoggedIn = isLoggedIn;
        if(this.username.equals("admin")){
            this.shoppingCart = null;
            this.cart = null;
            this.id = 0;
        }
        else{
            this.shoppingCart = new ShoppingCart();
            this.cart = cart;
        }
    }

    

    //getters
    public int getId(){
        return this.id;
    }

    public String getUsername(){
        return this.username;
    }

    public boolean getIsLoggedIn(){
        return this.isLoggedIn;
    }

    public Product[] getCart(){
        return this.cart;
    }

    public ShoppingCart getShoppingCart(){
        return this.shoppingCart;
    }

    //setters
    public void setUserName(String username){
        this.username = username;
    }

    public void setIsLoggedIn(boolean log){
        this.isLoggedIn = log;
    }

    // cart functionalities
    public boolean addToCart(Product p){
        if ((this.id != 0) && (isLoggedIn == true)){
            return shoppingCart.addToCart(p);
        }
        LOG.info("User - addToCart() failed. Invalid User or admin or logged off");
        return false;
    }
    

    public boolean removeFromCart(Product p){
        if((this.id !=0) && (isLoggedIn == true)){
            return shoppingCart.removeFromCart(p);
        }
        LOG.info("User - removeFromCart() failed. Invalid User or admin or logged off");
        return false;
    }

    //user not admin and can complete purchase
    public boolean completeOrder(){
        boolean b = false;
        if (this.id != 0 && isLoggedIn){
            if (shoppingCart.completePurchase()){
                b = true;
            }
        } else{
            LOG.info("User - completePurchase() failed. Invalid user or admin or logged off");
        }
        return b;
    }

    public boolean isUserAdmin(User user){
        if( user.getId() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return this.id + this.username.hashCode();
    }

    @Override
    public boolean equals(Object other){
        boolean r = false;
        if ( other instanceof User){
            User otherUser = (User) other;
            r = (this.username.equals(otherUser.username)) && this.id == otherUser.id;
        }
        return r;
    }
}
