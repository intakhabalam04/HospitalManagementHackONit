package com.intakhab.hospitalmanagementhackonit.Controller;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PaymentController {
    String createOrder(Map<String , Object> data);
    ResponseEntity<?> verifyPayment(Map<String , Object> data);
}
