package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static reactor.core.publisher.Mono.when;

public class BookOrdersServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;

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
}
