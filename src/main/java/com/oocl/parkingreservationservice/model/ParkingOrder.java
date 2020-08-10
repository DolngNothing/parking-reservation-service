package com.oocl.parkingreservationservice.model;
import javax.persistence.*;

@Entity
public class ParkingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer fetchNumber;
    private String startTime;
    private String endTime;
    private Integer userId;
    private Integer parkingLotId;
    private String createTime;
    private String status;
    private String carNumber;

    public ParkingOrder(Integer id, Integer fetchNumber, String startTime, String endTime, Integer userId, Integer parkingLotId, String createTime, String status, String carNumber) {
        this.id = id;
        this.fetchNumber = fetchNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.parkingLotId = parkingLotId;
        this.createTime = createTime;
        this.status = status;
        this.carNumber = carNumber;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
}
