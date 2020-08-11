package com.oocl.parkingreservationservice.controller;

import com.oocl.parkingreservationservice.dto.ParkingOrderRequest;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.IllegalOrderOperationException;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;
import com.oocl.parkingreservationservice.exception.ParkingOrderException;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.service.ParkingOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/parkingOrders")
public class ParkingOrderController {

    private final ParkingOrderService parkingOrderService;

    public ParkingOrderController(ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }

    @PatchMapping(value = "/{parkingOrderId}", params = {"type"})
    @ResponseStatus(HttpStatus.OK)
    public ParkingOrderResponse confirmParkingOrder(@PathVariable Integer parkingOrderId, String type) throws OrderNotExistException, IllegalOrderOperationException {
        return parkingOrderService.confirmParkingOrder(parkingOrderId);
    }

    @PatchMapping("/{parkingOrderId}")
    @ResponseStatus(HttpStatus.OK)
    public ParkingOrderResponse updateParkingOrder(@PathVariable Integer parkingOrderId) throws ParkingOrderException, ParseException, OrderNotExistException {
        return parkingOrderService.cancelOrder(parkingOrderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingOrderResponse addParkingOrder(@RequestBody ParkingOrderRequest parkingOrderRequest) throws IllegalParameterException {
        ParkingOrder parkingOrder = ParkingOrderMapper.convertToParkingOrder(parkingOrderRequest);
        return parkingOrderService.addParkingOrder(parkingOrder, parkingOrderRequest.getPhone(), parkingOrderRequest.getEmail());
    }

    @GetMapping("/{id}")
    public ParkingOrderResponse getOrderById(@PathVariable Integer id) throws ParkingOrderException {
        return parkingOrderService.getOrderById(id);
    }
}
