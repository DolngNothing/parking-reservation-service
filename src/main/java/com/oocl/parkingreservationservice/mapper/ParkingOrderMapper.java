package com.oocl.parkingreservationservice.mapper;

import com.oocl.parkingreservationservice.dto.ParkingOrderRequest;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import org.springframework.beans.BeanUtils;

public class ParkingOrderMapper {
    public static ParkingOrderResponse convertParkingOrderToParkingOrderResponse(ParkingOrder parkingOrder) {
        ParkingOrderResponse parkingOrderResponse = new ParkingOrderResponse();
        BeanUtils.copyProperties(parkingOrder, parkingOrderResponse);
        return parkingOrderResponse;
    }

    public static ParkingOrder convertToParkingOrder(ParkingOrderRequest parkingOrderRequest) {
        ParkingOrder parkingOrder = new ParkingOrder();
        BeanUtils.copyProperties(parkingOrderRequest, parkingOrder);
        return parkingOrder;
    }


}
