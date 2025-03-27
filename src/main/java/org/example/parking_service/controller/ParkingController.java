package org.example.parking_service.controller;


import org.example.parking_service.model.ParkingSlot;
import org.example.parking_service.service.ParkingSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingSlotService parkingService;

    @PostMapping("/checkin")
    public ResponseEntity<ParkingSlot> checkIn(@RequestParam String slotNumber,
                                               @RequestParam String vehicleNumber,
                                               @RequestParam Long userId) {
        ParkingSlot slot = parkingService.occupySlot(slotNumber, vehicleNumber, userId);
        return ResponseEntity.ok(slot);
    }

    @GetMapping("/availability")
    public ResponseEntity<List<ParkingSlot>> availability(){
        List<ParkingSlot> allParkingSlots = parkingService.getAllParkingSlots();

        if (allParkingSlots.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no available slots
        }

        return ResponseEntity.ok(allParkingSlots);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutVehicle(@RequestParam String slotNumber) {
        try {
            parkingService.freeSlot(slotNumber);
            return ResponseEntity.ok("Slot " + slotNumber + " is now vacant.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}