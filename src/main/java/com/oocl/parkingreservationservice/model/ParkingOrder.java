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
    private int id;
    private int fetchNumber;
    private String startTime;
    private String endTime;
    private int userId;
    private int parkingLotId;
    private String createTime;

}
