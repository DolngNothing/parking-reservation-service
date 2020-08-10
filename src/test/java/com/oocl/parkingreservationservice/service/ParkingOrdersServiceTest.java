package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class ParkingOrdersServiceTest {
    private ParkingOrderService parkingOrderService;
    private ParkingOrderRepository parkingOrderRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        parkingOrderRepository = Mockito.mock(ParkingOrderRepository.class);
        userRepository=Mockito.mock(UserRepository.class);
        parkingOrderService=new ParkingOrderService();
    }


    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_phone_number_123() {
        //given
        String illegalPhone="123";
        ParkingOrder parkingOrder=new ParkingOrder(null,1,"2020-08-10","2020-8-11",null,1,null, StatusContants.WAIT_FOR_SURE,"浙A1063警");

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder,illegalPhone));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_car_number_123() {
        //given
        String illegal_car_number="123";
        String phone="15920138477";
        ParkingOrder parkingOrder=new ParkingOrder(null,1,"2020-08-10","2020-8-11",null,1,null, StatusContants.WAIT_FOR_SURE,illegal_car_number);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> parkingOrderService.addParkingOrder(parkingOrder,phone));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
    }

}
