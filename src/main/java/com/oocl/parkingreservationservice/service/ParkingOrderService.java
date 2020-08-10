package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;

public class ParkingOrderService {

    private final ParkingOrderRepository parkingOrderRepository;

    public ParkingOrderService(ParkingOrderRepository parkingOrderRepository) {
        this.parkingOrderRepository = parkingOrderRepository;
    }

    public ParkingOrderResponse confirmParkingOrder(Integer orderId) {
        return null;
    }
}
