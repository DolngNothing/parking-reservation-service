package com.oocl.parkingreservationservice.controller;

import com.oocl.parkingreservationservice.exception.ParkingOrderException;
import com.oocl.parkingreservationservice.service.ParkingOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingOrders")
public class ParkingOrderController {

    private ParkingOrderService parkingOrderService;

    public ParkingOrderController(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }

    @PatchMapping("/{parkingOrderId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateParkingOrder(@PathVariable Integer OrderID) throws ParkingOrderException {
        parkingOrderService.cancelOrder(OrderID);
    }
}
