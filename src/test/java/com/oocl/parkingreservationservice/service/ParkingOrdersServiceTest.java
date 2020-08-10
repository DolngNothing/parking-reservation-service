package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        parkingOrderService=new ParkingOrderService(parkingOrderRepository);
    }

    @Test
    void should_return_confirm_parking_order_when_confirm_order_given_order_id() {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId,1,1,"2020-8-10 12:25:30","2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE,"1234");
        ParkingOrderResponse parkingOrderResponse;
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        given(parkingOrderRepository.findById(orderId)).willReturn(java.util.Optional.of(parkingOrder));
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository);
//        when
        parkingOrderResponse =  parkingOrderService.confirmParkingOrder(orderId);
//        then
        assertEquals(StatusContants.ALREADY_SURE,parkingOrderResponse.getStatus());
    }
    @Test
    void should_return_success_message_when_cancel_order_given_uncertain_order_id() {
        //given
        int orderId = 1;
        ParkingOrder order = new ParkingOrder(1,123,"2020-08-10","2020-8-11",1,1,"2020-08-10");
        order.set
        //when
        when(parkingOrderRepository.cancelOrder(orderId)).thenReturn();
        //then
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_phone_number_123() {
        //given
        String illegalPhone="123";
        String email="1214852999@qq.com";
        ParkingOrder parkingOrder=new ParkingOrder(null,1,"2020-08-10","2020-8-11",null,1,null, StatusContants.WAIT_FOR_SURE,"浙A1063警");

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
        ParkingOrder parkingOrder=new ParkingOrder(null,1,"2020-08-10","2020-8-11",null,1,null, StatusContants.WAIT_FOR_SURE,illegal_car_number);

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
        ParkingOrder parkingOrder=new ParkingOrder(null,1,"2020-08-10","2020-8-11",null,1,null, StatusContants.WAIT_FOR_SURE,"浙A1063警");

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder,phone,illegalEmail));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }
}
