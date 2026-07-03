package com.induwara.store.payments;

import com.induwara.store.orders.Order;

import java.util.Optional;

public interface PaymentGateway  {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
