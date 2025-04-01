package org.example.parking_service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("vehicleNumber")
    private String vehicleNUmber;

    private Long userId;

}
