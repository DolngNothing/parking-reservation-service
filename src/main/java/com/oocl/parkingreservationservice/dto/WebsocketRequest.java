package com.oocl.parkingreservationservice.dto;

import java.util.Objects;

public class WebsocketRequest {
    private Long startTime;
    private Long endTime;
    private Integer parkingLotId;

    public WebsocketRequest() {
    }

    public WebsocketRequest(Long startTime, Long endTime, Integer parkingLotId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.parkingLotId = parkingLotId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Integer parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Override
    public String toString() {
        return "Request{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", parkingLotId=" + parkingLotId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebsocketRequest that = (WebsocketRequest) o;
        return Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(parkingLotId, that.parkingLotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, parkingLotId);
    }
}
