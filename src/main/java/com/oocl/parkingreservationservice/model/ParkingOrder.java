package com.oocl.parkingreservationservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long fetchNumber;
    private String parkingStartTime;
    private String parkingEndTime;
    private Integer userId;
    private Integer parkingLotId;
    private String createTime;
    private String status;
    private String carNumber;
    private Double price;


}
