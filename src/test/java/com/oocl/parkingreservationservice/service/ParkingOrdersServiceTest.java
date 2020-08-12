package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderRequest;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.*;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class ParkingOrdersServiceTest {
    private ParkingOrderService parkingOrderService;
    private ParkingOrderRepository parkingOrderRepository;
    private UserRepository userRepository;
    private ParkingLotRepository parkingLotRepository;

    @BeforeEach
    public void init() {
        parkingOrderRepository = Mockito.mock(ParkingOrderRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        parkingLotRepository = Mockito.mock(ParkingLotRepository.class);
        parkingOrderService = new ParkingOrderService(parkingOrderRepository, userRepository, parkingLotRepository);
    }

    @Test
    void should_return_confirm_parking_order_when_confirm_order_given_order_id() throws IllegalOrderOperationException, OrderNotExistException {
        //given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, 1L, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);
        ParkingOrderResponse parkingOrderResponse;
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
//        when
        parkingOrderResponse = parkingOrderService.confirmParkingOrder(orderId);
//        then
        assertEquals(StatusContants.ALREADY_SURE, parkingOrderResponse.getStatus());
    }

    @Test
    void should_throw_wrong_message_when_confirm_order_given_order_id() {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, 1L, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.DELETED, "1234", 10.0);
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
//        when
        Exception exception = assertThrows(IllegalOrderOperationException.class, () -> parkingOrderService.confirmParkingOrder(orderId));
//        then
        assertEquals(MessageConstants.ODER_CANCELED, exception.getMessage());
    }

    @Test
    void should_throw_order_canceled_excption_when_confirm_order_given_confirmed_order_id() {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, 1L, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.ALREADY_SURE, "1234", 10.0);
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
//        when
        Exception exception = assertThrows(IllegalOrderOperationException.class, () -> parkingOrderService.confirmParkingOrder(orderId));
//        then
        assertEquals(MessageConstants.ODER_CONFIRMED, exception.getMessage());
    }

    @Test
    void should_throw_order_not_exist_exception_when_confirm_order_given_not_exist_order_id() {
//        given
        Integer orderId = 1;
        given(parkingOrderRepository.findById(orderId)).willReturn(Optional.empty());
//        when
        Exception exception = assertThrows(OrderNotExistException.class, () -> parkingOrderService.confirmParkingOrder(orderId));
//        then
        assertEquals(MessageConstants.ODER_NOT_EXIST, exception.getMessage());
    }

    @Test
    void should_return_updated_order_when_cancel_order_given_uncertain_order_id() throws ParkingOrderException, ParseException, OrderNotExistException {
        //given
        int orderId = 1;
        ParkingOrder order = new ParkingOrder(orderId, 1L, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);

        //when
        given(parkingOrderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(parkingOrderRepository.save(order)).willReturn(order);
        ParkingOrderResponse updateOrder = parkingOrderService.cancelOrder(orderId);
        //then
        assertEquals(StatusContants.DELETED, updateOrder.getStatus());
    }

    @Test
    void should_return_updated_order_when_cancel_order_given_certain_order_id() throws ParkingOrderException, ParseException, OrderNotExistException {
        //given
        int orderId = 1;
        ParkingOrderRequest order = new ParkingOrderRequest("2021-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, "A123", null, null, StatusContants.ALREADY_SURE);
        //when
        ParkingOrder parkingOrder = ParkingOrderMapper.convertToParkingOrder(order);
        given(parkingOrderRepository.findById(anyInt())).willReturn(Optional.of(parkingOrder));
        given(parkingOrderRepository.save(any(ParkingOrder.class))).willReturn(parkingOrder);
        ParkingOrderResponse updateOrder = parkingOrderService.cancelOrder(orderId);
        //then
        assertEquals(StatusContants.DELETED, updateOrder.getStatus());

    }

    @Test
    void should_throw_none_existent_exception_when_cancel_order_given_none_existent_order_id() {
        //given
        int orderId = 1;
        //when
        Exception parkingOrderException = assertThrows(ParkingOrderException.class, () -> parkingOrderService.cancelOrder(orderId));
        //then
        assertEquals(ParkingOrderException.class, parkingOrderException.getClass());
    }

    @Test
    void should_throw_already_cancel_exception_when_cancel_order_given_already_cancel_order_id() {
        //given
        int orderId = 1;
        ParkingOrder order = new ParkingOrder(orderId, 1L, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.DELETED, "1234", 10.0);
        //when
        given(parkingOrderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(parkingOrderRepository.save(order)).willReturn(order);
        Exception parkingOrderException = assertThrows(ParkingOrderException.class, () -> parkingOrderService.cancelOrder(orderId));
        //then
        assertEquals("订单已取消，请勿重复操作", parkingOrderException.getMessage());
        assertEquals(ParkingOrderException.class, parkingOrderException.getClass());
    }

    @Test
    void should_throw_outdate_exception_when_cancel_order_given_outdate_order_id() {
        //given
        int orderId = 1;
        ParkingOrder order = new ParkingOrder(orderId, 1L, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.ALREADY_SURE, "1234", 10.0);
        //when
        given(parkingOrderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(parkingOrderRepository.save(order)).willReturn(order);
        Exception parkingOrderException = assertThrows(ParkingOrderException.class, () -> parkingOrderService.cancelOrder(orderId));
        //then
        assertEquals("时间段已过期，无法取消", parkingOrderException.getMessage());
        assertEquals(ParkingOrderException.class, parkingOrderException.getClass());
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_phone_number_123() {
        //given
        String illegalPhone = "123";
        String email = "1214852999@qq.com";
        String parkingStartTime = Long.toString(new Date().getTime() + 10000);
        String parkingEndTime = Long.toString(new Date().getTime() + 20000);
        ParkingOrder parkingOrder = new ParkingOrder(null, null, parkingStartTime, parkingEndTime, null, 1, null, null, "浙A1063警", null);

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
        String parkingStartTime = Long.toString(new Date().getTime() + 1000);
        String parkingEndTime = Long.toString(new Date().getTime() + 2000);
        ParkingOrder parkingOrder = new ParkingOrder(null, null, parkingStartTime, parkingEndTime, null, 1, null, null, illegal_car_number, null);

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
        String parkingStartTime = Long.toString(new Date().getTime() + 1000);
        String parkingEndTime = Long.toString(new Date().getTime() + 2000);
        ParkingOrder parkingOrder = new ParkingOrder(null, null, parkingStartTime, parkingEndTime, null, 1, null, null, "浙A1063警", null);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder, phone, illegalEmail));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_time() {
        //given
        String email = "1214852999@qq.com";
        String phone = "15920138477";
        String parkingStartTime = Long.toString(new Date().getTime() + 1000);
        String parkingEndTime = Long.toString(new Date().getTime() + 500);
        ParkingOrder parkingOrder = new ParkingOrder(null, null, parkingStartTime, parkingEndTime, null, 1, null, null, "浙A1063警", null);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder, phone, email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }


    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_start_time_before_now() {
        //given
        String email = "1214852999@qq.com";
        String phone = "15920138477";
        String parkingStartTime = Long.toString(new Date().getTime() - 1000);
        String parkingEndTime = Long.toString(new Date().getTime() + 2000);
        ParkingOrder parkingOrder = new ParkingOrder(null, null, parkingStartTime, parkingEndTime, null, 1, null, null, "浙A1063警", null);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder, phone, email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }


    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_end_time_before_now() {
        //given
        String email = "1214852999@qq.com";
        String phone = "15920138477";
        String parkingStartTime = Long.toString(new Date().getTime() - 10000);
        String parkingEndTime = Long.toString(new Date().getTime() - 2000);
        ParkingOrder parkingOrder = new ParkingOrder(null, null, parkingStartTime, parkingEndTime, null, 1, null, null, "浙A1063警", null);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder, phone, email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_not_exist_parking_lot() {
        //given
        String email = "1214852999@qq.com";
        String phone = "15920138477";
        String parkingStartTime = Long.toString(new Date().getTime() + 1000);
        String parkingEndTime = Long.toString(new Date().getTime() + 2000);
        ParkingOrder parkingOrder = new ParkingOrder(null, null, parkingStartTime, parkingEndTime, null, 1, null, null, "浙A1063警", null);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder, phone, email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

    @Test
    void should_add_new_book_order_when_book_parking_lot_given_new_book_order() throws IllegalParameterException {
        //given
        String email = "1214852999@qq.com";
        String phone = "15920138477";
        String parkingStartTime = Long.toString(new Date().getTime() + 1000);
        String parkingEndTime = Long.toString(new Date().getTime() + 2000);
        ParkingOrder parkingOrder = new ParkingOrder(null, 1L, parkingStartTime, parkingEndTime, null, 1, null, null, "浙A1063警", 10.0);

        given(userRepository.findFirstByEmail(email)).willReturn(new User(1, null, email, "Jamea", "9999"));
        given(parkingLotRepository.findById(1)).willReturn(Optional.of(new ParkingLot(1, "test_parking_lot", "113.22", "22.3", 100, 1.5, null, null, "")));
        ParkingOrder mockedParkingOrder = new ParkingOrder(null, null, parkingStartTime, parkingEndTime, 1, 1, null, null, "浙A1063警", null);
        given(parkingOrderRepository.save(parkingOrder)).willReturn(mockedParkingOrder);
        //when
        ParkingOrderResponse returnParkingOrder = parkingOrderService.addParkingOrder(parkingOrder, phone, email);


        //then
        assertEquals(1, returnParkingOrder.getUserId());

    }

    @Test
    void should_return_certain_order_when_get_certain_order_given_order_id() throws ParkingOrderException {
        //given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, 1L, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);

        //when
        when(parkingOrderRepository.findById(1)).thenReturn(Optional.of(parkingOrder));
        ParkingOrderResponse parkingOrderResponse = parkingOrderService.getOrderById(orderId);

        //then
        assertEquals(ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(parkingOrder),parkingOrderResponse);
    }

    @Test
    void should_return_all_orders_when_get_all_orders_given_right_email() throws InquiryOrderException {
        //given
        String email = "545759585@qq.com";
        User user = new User(1,"15626155019",email,"karen","123");
        List<ParkingOrder> parkingOrders = Arrays.asList(
                new ParkingOrder(1, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0),
                new ParkingOrder(2, 1L, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤B258警", 30.0)
        );
        given(userRepository.findByEmail(email)).willReturn(user);
        given(parkingOrderRepository.findAllByUserId(1)).willReturn(parkingOrders);

        //when
        List<ParkingOrderResponse> parkingOrderByEmail = parkingOrderService.getAllOrdersByEmail(email);

        //then
        assertEquals(parkingOrders.stream().map(ParkingOrderMapper::convertParkingOrderToParkingOrderResponse).collect(Collectors.toList()), parkingOrderByEmail);
    }

    @Test
    void should_throw_inquiryOrderException_exception_when_inquiry_order_given_not_exist_email() {
        //given
        String email = "545759585@qq.com";
        String unExistEmail = "695759689@qq.com";
        User user = new User(1,"15626155019",email,"karen","123");
        List<ParkingOrder> parkingOrders = Arrays.asList(
                new ParkingOrder(1, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0),
                new ParkingOrder(2, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤B258警", 30.0)
        );
        given(userRepository.findByEmail(email)).willReturn(user);
        given(parkingOrderRepository.findAllByUserId(1)).willReturn(parkingOrders);

        //when
        Exception exception = assertThrows(InquiryOrderException.class, () -> parkingOrderService.getAllOrdersByEmail(unExistEmail));

        //then
        assertEquals("指定email不存在，请输入正确的email", exception.getMessage());
    }

    @Test
    void should_return_all_orders_when_get_all_orders_given_right_phoneNumber() throws InquiryOrderException {
        //given
        String phoneNumber = "15626155019";
        User user = new User(1,"15626155019",null,"karen","123");
        List<ParkingOrder> parkingOrders = Arrays.asList(
                new ParkingOrder(1, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0),
                new ParkingOrder(2, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤B258警", 30.0)
        );
        given(userRepository.findByPhoneNumber(phoneNumber)).willReturn(user);
        given(parkingOrderRepository.findAllByUserId(1)).willReturn(parkingOrders);

        //when
        List<ParkingOrderResponse> parkingOrderByPhone = parkingOrderService.getAllOrdersByPhoneNumber(phoneNumber);

        //then
        assertEquals(parkingOrders.stream().map(ParkingOrderMapper::convertParkingOrderToParkingOrderResponse).collect(Collectors.toList()), parkingOrderByPhone);
    }

    @Test
    void should_throw_inquiryOrderException_exception_when_inquiry_order_given_not_exist_phoneNumber() {
        //given
        String phoneNumber = "15626155019";
        String unExistphoneNumber = "13676242112";
        User user = new User(1,phoneNumber,null,"karen","123");
        List<ParkingOrder> parkingOrders = Arrays.asList(
                new ParkingOrder(1, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0),
                new ParkingOrder(2, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤B258警", 30.0)
        );
        given(userRepository.findByPhoneNumber(phoneNumber)).willReturn(user);
        given(parkingOrderRepository.findAllByUserId(1)).willReturn(parkingOrders);

        //when
        Exception exception = assertThrows(InquiryOrderException.class, () -> parkingOrderService.getAllOrdersByPhoneNumber(unExistphoneNumber));

        //then
        assertEquals("指定phoneNumber不存在，请输入正确的phoneNumber", exception.getMessage());
    }

    @Test
    void should_throw_inquiryOrderException_exception_when_inquiry_order_given_not_exist_userId() {
        //given
        Integer userId = 1;
        Integer unExistUserId = 826;
        User user = new User(userId,null,null,"karen","123");
        List<ParkingOrder> parkingOrders = Arrays.asList(
                new ParkingOrder(1, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0),
                new ParkingOrder(2, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤B258警", 30.0)
        );
        given(parkingOrderRepository.findAllByUserId(userId)).willReturn(parkingOrders);

        //when
        Exception exception = assertThrows(InquiryOrderException.class, () -> parkingOrderService.getAllOrdersByUserId(unExistUserId));

        //then
        assertEquals("指定userID不存在，请输入正确的userID", exception.getMessage());
    }

}
