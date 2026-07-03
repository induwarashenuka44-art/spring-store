package com.induwara.store.carts;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(){
        super("Cart was not found");
    }
}
