package org.example.parking_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckinRequest {

    private String slotNumber;
    private String vehicleNUmber;
    private Long userId;

}
