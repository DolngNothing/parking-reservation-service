package com.oocl.parkingreservationservice.controller;

import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.IllegalOrderOperationException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;
import com.oocl.parkingreservationservice.exception.ParkingOrderException;
import com.oocl.parkingreservationservice.service.ParkingOrderService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingOrders")
public class ParkingOrderController {

    private ParkingOrderService parkingOrderService;

    public ParkingOrderController(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }
    @PatchMapping(value = "/{parkingOrderId}",params = {"type"})
    @ResponseStatus(HttpStatus.OK)
    public ParkingOrderResponse confirmParkingOrder(@PathVariable Integer parkingOrderId, String type) throws OrderNotExistException, IllegalOrderOperationException {
        return parkingOrderService.confirmParkingOrder(parkingOrderId);
    }
    @PatchMapping("/{parkingOrderId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateParkingOrder(@PathVariable Integer parkingOrderId) throws ParkingOrderException {
        parkingOrderService.cancelOrder(parkingOrderId);
    }
}
