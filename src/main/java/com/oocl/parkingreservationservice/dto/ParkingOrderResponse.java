package com.oocl.parkingreservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
