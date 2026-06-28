package com.induwara.store.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(){
        super("Cart is empty");
    }
}
