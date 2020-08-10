package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;

import java.util.Optional;

public class ParkingOrderService {
    private ParkingOrderRepository parkingOrderRepository;

    public ParkingOrderService(ParkingOrderRepository parkingOrderRepository) {
        this.parkingOrderRepository = parkingOrderRepository;
    }

    public Optional<ParkingOrder> cancelOrder(int orderId) {
        Optional<ParkingOrder> order = parkingOrderRepository.findById(orderId);
        return order;
//        if(order.isPresent()){
//            ParkingOrder oldOrder = order.get();
//        }
    }
}
