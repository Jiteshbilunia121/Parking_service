package org.example.parking_service.event;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ParkingEventProducer {


    private final KafkaTemplate<String, CheckoutEvent> eventKafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    public ParkingEventProducer(KafkaTemplate<String, CheckoutEvent> eventKafkaTemplate, KafkaTemplate<String, String> stringKafkaTemplate) {
        this.eventKafkaTemplate = eventKafkaTemplate;
        this.stringKafkaTemplate = stringKafkaTemplate;
    }

    public void sendParkingSlotVacantEvent(CheckoutEvent event) {
       eventKafkaTemplate.send("parking.slot.vacant", event);
    }
    public void sendParkingSlotOccupiedEvent(String eventMessage) {
        stringKafkaTemplate.send("parking.slot.occupied",eventMessage);
    }
}