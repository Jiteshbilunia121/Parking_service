package org.example.parking_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parking_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slotNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SlotStatus status; // ENUM (OCCUPIED / VACANT)

    @Column
    private String vehicleNumber; // Nullable when vacant

    @Column
    private Long userId; // Nullable when vacant
}
