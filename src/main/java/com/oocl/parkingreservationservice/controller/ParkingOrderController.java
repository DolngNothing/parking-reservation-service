package com.oocl.parkingreservationservice.controller;

import com.oocl.parkingreservationservice.dto.ParkingOrderRequest;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.*;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.service.ParkingOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

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
    public ParkingOrderResponse updateParkingOrder(@PathVariable Integer parkingOrderId) throws ParkingOrderException, ParseException {
        return parkingOrderService.cancelOrder(parkingOrderId);
    }
//TODO 返回邮箱手机，修改代码
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingOrderResponse addParkingOrder(@RequestBody ParkingOrderRequest parkingOrderRequest) throws IllegalParameterException, UserNotExistException {
        ParkingOrder parkingOrder = ParkingOrderMapper.convertToParkingOrder(parkingOrderRequest);
        return parkingOrderService.addParkingOrder(parkingOrder, parkingOrderRequest.getPhone(), parkingOrderRequest.getEmail());
    }

    @GetMapping("/{id}")
    public ParkingOrderResponse getOrderById(@PathVariable Integer id) throws ParkingOrderException {
        return parkingOrderService.getOrderById(id);
    }

    @GetMapping(params = {"userId"})
    @ResponseStatus(HttpStatus.OK)
    public List<ParkingOrderResponse> getAllOrderById(@RequestParam(name = "userId") Integer userId) throws InquiryOrderException {
        return parkingOrderService.getAllOrdersByUserId(userId);
    }

    @GetMapping(params = {"email"})
    @ResponseStatus(HttpStatus.OK)
    public List<ParkingOrderResponse> getAllOrderByEmail(@RequestParam(name = "email") String email) throws InquiryOrderException {
        return parkingOrderService.getAllOrdersByEmail(email);
    }

    @GetMapping(params = {"phoneNumber"})
    @ResponseStatus(HttpStatus.OK)
    public List<ParkingOrderResponse> getAllOrderByPhoneNumber(@RequestParam(name = "phoneNumber") String phoneNumber) throws InquiryOrderException {
        return parkingOrderService.getAllOrdersByPhoneNumber(phoneNumber);
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public String getQRCodeByOrderId(@PathVariable Integer orderId){
        return parkingOrderService.getQRCode(orderId);
    }

}
