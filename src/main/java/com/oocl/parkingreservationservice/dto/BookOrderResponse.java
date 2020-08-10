package com.oocl.parkingreservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookOrderResponse {
    private Integer id;
    private Integer userId;
    private Integer parkingLotId;
    private String createTime;
    private String parkingLotName;
    private String location;
    private Double price;
    private String status;
    private String parkingStartTime;
    private String parkingEndTime;
}
