package org.example.parking_service.listener;

import org.example.parking_service.dto.PaymentRequest;
import org.example.parking_service.dto.PaymentResponse;
import org.example.parking_service.event.CheckoutEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Component
public class CheckoutEventListener {



//    @KafkaListener(topics = "Parking.slot.vacant", groupId = "parking-service-group")
//    public void handleCheckoutEvent(CheckoutEvent event) {
//
//        System.out.println("CheckoutEvent: " + event.toString());
//        // Process the event and trigger payment
//        // /pay api ko call
////        PaymentRequest paymentRequest = ;
//
//
//    }

    @Autowired
    private RestTemplate restTemplate;

    @KafkaListener(topics = "parking.slot.vacant")
    public void listen(@Payload CheckoutEvent event, @Header(KafkaHeaders.GROUP_ID) String groupId) {

        final String users_api = "http://USER-SERVICE/api/users/pay";
        System.out.println("Group ID: " + groupId);
        System.out.println("Checkout event: " + event.getSlotNumber() + " " + event.getVehicleNumber() + " " + event.getUserId() + " " + event.getAmount());

        PaymentRequest paymentRequest = new PaymentRequest(event.getUserId(), event.getVehicleNumber(), event.getAmount());

        // Call /pay API in User Service
        try {

            ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(
                    users_api,
                    paymentRequest,
                    PaymentResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Payment successful for vehicle: {}", event.getVehicleNumber());
            } else {
                log.error("Payment failed for vehicle: {}", event.getVehicleNumber());
            }

        } catch (Exception e) {
            log.error("Error calling User Service for payment", e);
        }
    }


}

