package org.example.parking_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

        private String transactionId;
        private boolean success;
        private String message;
        // Getters & Setters
}

