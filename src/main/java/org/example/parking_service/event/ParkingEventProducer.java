package org.example.parking_service.event;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ParkingEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ParkingEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendParkingSlotVacantEvent(String eventMessage) {
        kafkaTemplate.send("parking.slot.occupied",eventMessage);
    }
    public void sendParkingSlotOccupiedEvent(String eventMessage) {
        kafkaTemplate.send("parking.slot.occupied",eventMessage);
    }
}
