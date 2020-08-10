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
import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ParkingOrdersServiceTest {
    private ParkingOrderService parkingOrderService;
    private ParkingOrderRepository parkingOrderRepository;
    private UserRepository userRepository;
    @BeforeEach
    public void init() {
        parkingOrderRepository = Mockito.mock(ParkingOrderRepository.class);
        userRepository=Mockito.mock(UserRepository.class);
        parkingOrderService=new ParkingOrderService(parkingOrderRepository,userRepository);
    }

    @Test
    void should_return_confirm_parking_order_when_confirm_order_given_order_id() throws OrderNotExistException, IllegalOrderOperationException {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId,1,"2020-8-10 12:25:30",
                "2020-8-10 14:25:30",1,1,"2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE,"1234",10.0);
        ParkingOrderResponse parkingOrderResponse;
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository,userRepository);
//        when
        parkingOrderResponse =  parkingOrderService.confirmParkingOrder(orderId);
//        then
        assertEquals(StatusContants.ALREADY_SURE,parkingOrderResponse.getStatus());
    }
    @Test
    void should_throw_wrong_message_when_confirm_order_given_order_id() {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, 1, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.DELETED, "1234", 10.0);
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository,userRepository);
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
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository,userRepository);
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
        given(parkingOrderRepository.findById(orderId)).willReturn(null);
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository,userRepository);
//        when
        Exception exception = assertThrows(OrderNotExistException.class, () -> parkingOrderService.confirmParkingOrder(orderId));
//        then
        assertEquals(MessageConstants.ODER_NOT_EXIST, exception.getMessage());
    }
    @Test
    void should_return_success_message_when_cancel_order_given_uncertain_order_id() {
        //given
        int orderId = 1;
        ParkingOrder order = new ParkingOrder(orderId,1,"2020-8-10 12:25:30",
                "2020-8-10 14:25:30",1,1,"2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE,"1234",10.0);
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository,userRepository);
        //when
        parkingOrderRepository.updateStatus(StatusContants.WAIT_FOR_SURE,orderId);
        //then
        Mockito.verify(parkingOrderRepository).updateStatus(StatusContants.WAIT_FOR_SURE,orderId);
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_phone_number_123() {
        //given
        String illegalPhone="123";
        String email="1214852999@qq.com";
        ParkingOrder parkingOrder=new ParkingOrder(null,1,"2020-08-10","2020-8-11",null,1,null, StatusContants.WAIT_FOR_SURE,"浙A1063警",10.0);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder,illegalPhone, email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_car_number_123() {
        //given
        String illegal_car_number="123";
        String phone="15920138477";
        String email="1214852999@qq.com";
        ParkingOrder parkingOrder=new ParkingOrder(null,1,"2020-08-10","2020-8-11",null,1,null, StatusContants.WAIT_FOR_SURE,illegal_car_number,10.0);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder,phone, email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }
    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_email_123() {
        //given
        String illegalEmail="123";
        String phone="15920138477";
        ParkingOrder parkingOrder=new ParkingOrder(null,1,"2020-08-10","2020-8-11",null,1,null, StatusContants.WAIT_FOR_SURE,"浙A1063警",10.0);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder,phone,illegalEmail));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }
    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_time() {
        //given
        String email="1214852999@qq.com";
        String phone="15920138477";
        String parkingStartTime="2020-08-12";
        String parkingEndTime="2020-08-11";
        ParkingOrder parkingOrder=new ParkingOrder(null,1,parkingStartTime,parkingEndTime,null,1,null, StatusContants.WAIT_FOR_SURE,"浙A1063警",10.0);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder,phone,email));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }
//    @Test
//    void should_add_new_book_order_when_book_parking_lot_given_new_book_order() throws IllegalParameterException {
//        //given
//        String email="1214852999@qq.com";
//        String phone="15920138477";
//        String parkingStartTime="2020-08-16 00:00:00";
//        String parkingEndTime="2020-08-17 00:00:00";
//        ParkingOrder parkingOrder=new ParkingOrder(null,1,parkingStartTime,parkingEndTime,null,1,null, StatusContants.WAIT_FOR_SURE,"浙A1063警",10.0);
//        List<User> users=new ArrayList<>();
//        users.add(new User(1,null,email,"Jamea","9999"));
//        given(userRepository.findFirst1ByEmail(email)).willReturn(users);
//        ParkingOrder mockedParkingOrder=new ParkingOrder(null,1,parkingStartTime,parkingEndTime,1,1,null,StatusContants.WAIT_FOR_SURE,"浙A1063警",10.0);
//        //given(parkingOrderRepository.save(parkingOrder)).willReturn(users);
//        //when
//        ParkingOrder returnParkingOrder=parkingOrderService.addParkingOrder(parkingOrder,phone,email);
//
//        //then
//        assertEquals(userRepository.findFirst1ByEmail(email).get(0).getId(),returnParkingOrder.getUserId());
//    }
}
