package org.example.parking_service.controller;


import org.example.parking_service.dto.CheckinRequest;
import org.example.parking_service.dto.CheckoutRequest;
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

//
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<String> home() {
//            return ResponseEntity.ok("Parking Service is running!");
        return ResponseEntity.ok("Parking service running");
    }



    @PostMapping("/checkin")
    public ResponseEntity<ParkingSlot> checkIn(@RequestBody CheckinRequest checkinRequest) {
        String slotNumber = checkinRequest.getSlotNumber();
        String vehicleNumber = checkinRequest.getVehicleNUmber();
        Long userId = checkinRequest.getUserId();
        System.out.println("slot number: " + slotNumber);
        System.out.println("User id: " + userId);
        System.out.println("vehicle number " + checkinRequest.getVehicleNUmber());

        ParkingSlot slot = parkingService.occupySlot(slotNumber, vehicleNumber, userId);
        return ResponseEntity.ok(slot);
    }
//    @PostMapping("/checkin")
//    public ResponseEntity<ParkingSlot> checkin(@RequestParam String slotNumber, @RequestParam String vehicleNumber, @RequestParam Long userId) {
//        System.out.println(slotNumber+" "+vehicleNumber+" "+userId);
//        ParkingSlot slot = parkingService.occupySlot(slotNumber, vehicleNumber, userId);
//        return new ResponseEntity<>(slot, HttpStatus.OK);
//    }

    @GetMapping("/availability")
    public ResponseEntity<List<ParkingSlot>> availability(){
        List<ParkingSlot> allParkingSlots = parkingService.getAllParkingSlots();

        if (allParkingSlots.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no available slots
        }

        return ResponseEntity.ok(allParkingSlots);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutVehicle(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            parkingService.freeSlot(checkoutRequest.getSlotNumber());
            return ResponseEntity.ok("Slot " + checkoutRequest.getSlotNumber() + " is now vacant.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}