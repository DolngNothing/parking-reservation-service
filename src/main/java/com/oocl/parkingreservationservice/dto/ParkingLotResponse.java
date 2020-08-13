package com.oocl.parkingreservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XUAL7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotResponse {
    private Integer id;
    private String name;
    private String latitude;
    private String longitude;
    private Integer capacity;
    private Double price;
    private String description;
    private String imageUrl;
    private String location;
    private Double distance;
    private Double avgScore;
}
