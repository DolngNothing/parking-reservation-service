package com.oocl.parkingreservationservice.controller;

import com.oocl.parkingreservationservice.exception.ParkingOrderException;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.service.ParkingOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/parkingOrders")
public class ParkingOrderController {

    private ParkingOrderService parkingOrderService;

    public ParkingOrderController(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }

    @PatchMapping("/{parkingOrderId}")
    @ResponseStatus(HttpStatus.OK)
    public ParkingOrder updateParkingOrder(@PathVariable Integer parkingOrderId) throws ParkingOrderException, ParseException {
        return parkingOrderService.cancelOrder(parkingOrderId);
    }
}
