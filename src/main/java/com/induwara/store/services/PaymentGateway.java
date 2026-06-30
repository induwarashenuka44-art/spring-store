package com.induwara.store.services;

import com.induwara.store.entities.Order;

public interface PaymentGateway  {
    CheckoutSession createCheckoutSession(Order order);
}
