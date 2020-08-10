package com.oocl.parkingreservationservice.model;
import javax.persistence.*;

@Entity
public class ParkingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer fetchNumber;
    private String parkingStartTime;
    private String parkingEndTime;
    private Integer userId;
    private Integer parkingLotId;
    private String createTime;
    private String status;
    private String carNumber;
    private Double price;
    public ParkingOrder() {

    }
    public ParkingOrder(Integer id, Integer fetchNumber, String parkingStartTime, String parkingEndTime, Integer userId, Integer parkingLotId, String createTime, String status, String carNumber, Double price) {
        this.id = id;
        this.fetchNumber = fetchNumber;
        this.parkingStartTime = parkingStartTime;
        this.parkingEndTime = parkingEndTime;
        this.userId = userId;
        this.parkingLotId = parkingLotId;
        this.createTime = createTime;
        this.status = status;
        this.carNumber = carNumber;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFetchNumber() {
        return fetchNumber;
    }

    public void setFetchNumber(Integer fetchNumber) {
        this.fetchNumber = fetchNumber;
    }

    public String getParkingStartTime() {
        return parkingStartTime;
    }

    public void setParkingStartTime(String parkingStartTime) {
        this.parkingStartTime = parkingStartTime;
    }

    public String getParkingEndTime() {
        return parkingEndTime;
    }

    public void setParkingEndTime(String parkingEndTime) {
        this.parkingEndTime = parkingEndTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Integer parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
