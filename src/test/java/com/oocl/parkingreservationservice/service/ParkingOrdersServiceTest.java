package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.IllegalOrderOperationException;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ParkingOrdersServiceTest {
    private ParkingOrderService parkingOrderService;
    private ParkingOrderRepository parkingOrderRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        parkingOrderRepository = Mockito.mock(ParkingOrderRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        parkingOrderService = new ParkingOrderService(parkingOrderRepository);
    }

    @Test
    void should_return_confirm_parking_order_when_confirm_order_given_order_id() throws IllegalOrderOperationException, OrderNotExistException {
//        given
        Integer orderId = 1;

        ParkingOrder parkingOrder = new ParkingOrder(orderId, 1, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);
        ParkingOrderResponse parkingOrderResponse;
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository);
//        when
        parkingOrderResponse = parkingOrderService.confirmParkingOrder(orderId);
//        then
        assertEquals(StatusContants.ALREADY_SURE, parkingOrderResponse.getStatus());
    }

    @Test
    void should_throw_wrong_message_when_confirm_order_given_order_id() {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, 1, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.DELETED, "1234", 10.0);
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository);
//        when
        Exception exception = assertThrows(IllegalOrderOperationException.class, () -> parkingOrderService.confirmParkingOrder(orderId));
//        then
        assertEquals(MessageConstants.ODER_CANCELED, exception.getMessage());
    }

    @Test
    void should_throw_order_canceled_excption_when_confirm_order_given_confirmed_order_id() {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, 1, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.ALREADY_SURE, "1234", 10.0);
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository);
//        when
        Exception exception = assertThrows(IllegalOrderOperationException.class, () -> parkingOrderService.confirmParkingOrder(orderId));
//        then
        assertEquals(MessageConstants.ODER_CONFIRMED, exception.getMessage());
    }
    @Test
    void should_throw_order_not_exist_excption_when_confirm_order_given_not_exist_order_id() {
//        given
        Integer orderId = 1;
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        given(parkingOrderRepository.findById(orderId)).willReturn(Optional.empty());
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository);
//        when
        Exception exception = assertThrows(OrderNotExistException.class, () -> parkingOrderService.confirmParkingOrder(orderId));
//        then
        assertEquals(MessageConstants.ODER_NOT_EXIST, exception.getMessage());
    }
    @Test
    void should_return_success_message_when_cancel_order_given_uncertain_order_id() {
        //given
        int orderId = 1;
        ParkingOrder order = new ParkingOrder(orderId, 1, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository);
        //when
        parkingOrderRepository.updateStatus(StatusContants.WAIT_FOR_SURE, orderId);
        //then
        Mockito.verify(parkingOrderRepository).updateStatus(StatusContants.WAIT_FOR_SURE, orderId);
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_phone_number_123() {
        //given
        String illegalPhone = "123";
        String email = "1214852999@qq.com";
        ParkingOrder parkingOrder = new ParkingOrder(null, 1, "2020-08-10", "2020-8-11", null, 1, null, StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder, illegalPhone, email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_car_number_123() {
        //given
        String illegal_car_number = "123";
        String phone = "15920138477";
        String email = "1214852999@qq.com";
        ParkingOrder parkingOrder = new ParkingOrder(null, 1, "2020-08-10", "2020-8-11", null, 1, null, StatusContants.WAIT_FOR_SURE, illegal_car_number, 10.0);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder, phone, email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_email_123() {
        //given
        String illegalEmail = "123";
        String phone = "15920138477";
        ParkingOrder parkingOrder = new ParkingOrder(null, 1, "2020-08-10", "2020-8-11", null, 1, null, StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder, phone, illegalEmail));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

}
