package com.oocl.parkingreservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingOrderResponse {
    private Integer id;
    private Integer userId;
    private Integer parkingLotId;
    private String parkingStartTime;
    private String parkingEndTime;
    private Integer fetchNumber;
    private String status;
    private String carNumber;
    private String createTime;
    private String parkingLotName;
    private String location;
    private Double price;


}
