package com.oocl.parkingreservationservice.mapper;

import com.oocl.parkingreservationservice.dto.ParkingLotResponse;
import com.oocl.parkingreservationservice.model.ParkingLot;
import org.springframework.beans.BeanUtils;

/**
 * @author xhz
 */
public class ParkingLotMapper {
    public static ParkingLotResponse map(ParkingLot parkingLot) {
        ParkingLotResponse parkingLotResponse = new ParkingLotResponse();
        BeanUtils.copyProperties(parkingLot, parkingLotResponse);
        return parkingLotResponse;
    }
}
