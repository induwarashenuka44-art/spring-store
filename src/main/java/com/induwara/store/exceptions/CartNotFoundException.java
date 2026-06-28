package com.induwara.store.exceptions;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(){
        super("Cart was not found");
    }
}
