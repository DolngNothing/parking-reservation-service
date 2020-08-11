package com.oocl.parkingreservationservice.dto;


import java.util.Objects;

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


    public ParkingOrderResponse() {

    }

    public ParkingOrderResponse(Integer id, Integer userId, Integer parkingLotId, String parkingStartTime, String parkingEndTime, String status, String carNumber) {
        this.id = id;
        this.userId = userId;
        this.parkingLotId = parkingLotId;
        this.parkingStartTime = parkingStartTime;
        this.parkingEndTime = parkingEndTime;
        this.status = status;
        this.carNumber = carNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getFetchNumber() {
        return fetchNumber;
    }

    public void setFetchNumber(Integer fetchNumber) {
        this.fetchNumber = fetchNumber;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingOrderResponse that = (ParkingOrderResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(parkingLotId, that.parkingLotId) &&
                Objects.equals(parkingStartTime, that.parkingStartTime) &&
                Objects.equals(parkingEndTime, that.parkingEndTime) &&
                Objects.equals(fetchNumber, that.fetchNumber) &&
                Objects.equals(status, that.status) &&
                Objects.equals(carNumber, that.carNumber) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, parkingLotId, parkingStartTime, parkingEndTime, fetchNumber, status, carNumber, createTime);
    }
}
