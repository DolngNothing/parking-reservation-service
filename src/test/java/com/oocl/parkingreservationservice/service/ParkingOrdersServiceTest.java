package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ParkingOrdersServiceTest {
    private ParkingOrderService parkingOrderService;
    private ParkingOrderRepository parkingOrderRepository;

    @BeforeEach
    public void init() {
        orderRepository = Mockito.mock(OrderRepository.class);
    }


    @Test
    void should_return_success_message_when_cancel_order_given_uncertain_order_id() {
        //given
        int orderId = 1;
        ParkingOrder order = new ParkingOrder(1,123,"2020-08-10","2020-8-11",1,1,"2020-08-10");
        //when
        when(orderRepository.cancelOrder(orderId)).thenReturn();
        //then
    }

    @Test
    void should_throw_illegal_parameter_exception_when_book_parking_lot_given_illegal_phone_number_123() {
        //given
        String illegalPhone="123";
        ParkingOrder parkingOrder=new ParkingOrder(null,1,1,)
        //when

        //then
    }
}
