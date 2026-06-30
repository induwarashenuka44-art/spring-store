package com.induwara.store.controllers;

import com.induwara.store.dtos.CheckoutRequest;
import com.induwara.store.dtos.CheckoutResponse;
import com.induwara.store.dtos.ErrorDto;
import com.induwara.store.exceptions.CartEmptyException;
import com.induwara.store.exceptions.CartNotFoundException;
import com.induwara.store.exceptions.PaymentException;
import com.induwara.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping ("/checkout")
public class CheckoutController {
    private final CheckoutService  checkoutService;

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request){
            return checkoutService.checkoutResponse(request);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

}
