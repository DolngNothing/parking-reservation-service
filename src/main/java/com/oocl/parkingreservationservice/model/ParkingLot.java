package com.oocl.parkingreservationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author XUAL7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ParkingLot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String latitude;
    private String longitude;
    private Integer capacity;
    private Double price;
    private String description;
    private String imageUrl;
    private String location;
    private String type;
}
