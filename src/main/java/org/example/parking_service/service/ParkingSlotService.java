package org.example.parking_service.service;

import jakarta.transaction.Transactional;
import org.example.parking_service.event.CheckoutEvent;
import org.example.parking_service.event.ParkingEventProducer;
import org.example.parking_service.model.ParkingSlot;
import org.example.parking_service.model.SlotStatus;
import org.example.parking_service.repository.ParkingSlotRepository;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingEventProducer parkingEventProducer;



    public ParkingSlotService(ParkingSlotRepository parkingSlotRepository, ParkingEventProducer parkingEventProducer) {
        this.parkingSlotRepository = parkingSlotRepository;
        this.parkingEventProducer = parkingEventProducer;


    }
    public List<ParkingSlot> getAllParkingSlots() {
        return parkingSlotRepository.findByStatus(SlotStatus.VACANT);
    }
    @Transactional
    public ParkingSlot occupySlot(String slotNumber, String vehicleNumber,Long userId){
        Optional<ParkingSlot> optionalSlot = parkingSlotRepository.findBySlotNumber(slotNumber);
//        System.out.println("vehicle number " + optionalSlot.get().getVehicleNumber());
        if (optionalSlot.isPresent()) {
            ParkingSlot slot = optionalSlot.get();
            if (slot.getStatus() == SlotStatus.OCCUPIED) {
                throw new RuntimeException("Slot is already occupied!");
            }
            slot.setStatus(SlotStatus.OCCUPIED);
            slot.setVehicleNumber(vehicleNumber);
            slot.setUserId(userId);

            System.out.println("vehicle number " + optionalSlot.get().getVehicleNumber());

            ParkingSlot savedSlot = parkingSlotRepository.save(slot);


            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    String eventMessage = "User " + savedSlot.getUserId() + " parked at slot " + savedSlot.getSlotNumber();
                    parkingEventProducer.sendParkingSlotOccupiedEvent(eventMessage);
                }
            });

            return savedSlot;
        }
        else {
            throw new RuntimeException("Invalid Slot Number!");
        }

    }

    @Transactional
    public void freeSlot(String slotNumber) {
        Optional<ParkingSlot> optionalSlot = parkingSlotRepository.findBySlotNumber(slotNumber);
        if(optionalSlot.isPresent()) {
            ParkingSlot slot = optionalSlot.get();
            if(slot.getStatus() == SlotStatus.VACANT) {
                throw new RuntimeException("Slot is already vacant!");
            }
            CheckoutEvent event = new CheckoutEvent( slot.getVehicleNumber(),slot.getSlotNumber(), slot.getUserId(), 250);
            slot.setStatus(SlotStatus.VACANT);
            slot.setUserId(null);
            slot.setVehicleNumber(null);
            ParkingSlot savedSlot = parkingSlotRepository.save(slot);

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {

                    String eventMessage = "User" + savedSlot.getUserId() + " freed the slot " + savedSlot.getSlotNumber();
                    parkingEventProducer.sendParkingSlotVacantEvent(event);
                }
            });
        }
    }

//
//    @PostCommit // Custom event outside transaction
//    public void sendKafkaEvent(ParkingSlot slot) {
//        String eventMessage = "User " + slot.getUserId() + " parked at slot " + slot.getSlotNumber();
//        parkingEventProducer.sendParkingSlotOccupiedEvent(eventMessage);
//    }
//



}
