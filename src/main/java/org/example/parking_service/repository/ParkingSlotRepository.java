package org.example.parking_service.repository;

import org.example.parking_service.model.ParkingSlot;
import org.example.parking_service.model.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    Optional<ParkingSlot> findByVehicleNumber(String vehicleNumber);
    Optional<ParkingSlot> findBySlotNumber(String slotNumber);
    List<ParkingSlot> findByStatus(SlotStatus status);
}