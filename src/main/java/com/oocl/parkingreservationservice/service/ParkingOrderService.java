package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;

public class ParkingOrderService {
    private final ParkingOrderRepository parkingOrderRepository;
    public ParkingOrderService(ParkingOrderRepository parkingOrderRepository) {
        this.parkingOrderRepository =parkingOrderRepository;
    }

    public ParkingOrderResponse confirmParkingOrder(Integer orderId) {
        ParkingOrder parkingOrder = parkingOrderRepository.findById(orderId).orElse(null);
        assert parkingOrder != null;
        parkingOrder.setStatus(StatusContants.ALREADY_SURE);
        parkingOrderRepository.save(parkingOrder);
        return  ParkingOrderMapper.converToParkingOrderResponse(parkingOrder);
    }
}
