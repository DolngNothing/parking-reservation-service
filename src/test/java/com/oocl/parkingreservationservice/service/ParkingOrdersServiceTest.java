package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ParkingOrdersServiceTest {
    private ParkingOrderService parkingOrderService;
    private ParkingOrderRepository parkingOrderRepository;

    @BeforeEach
    public void init() {
        parkingOrderRepository = Mockito.mock(ParkingOrderRepository.class);
        parkingOrderService = new ParkingOrderService(parkingOrderRepository);
    }


    @Test
    void should_return_success_message_when_cancel_order_given_uncertain_order_id() {
        //given
        int orderId = 1;
        ParkingOrder order = new ParkingOrder(1,123,"2020-08-10","2020-8-11",1,1,"2020-08-10",0,"A123");
        //when
        when(parkingOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        ParkingOrder updateOrder = parkingOrderService.cancelOrder(orderId).orElse(null);
        //then
        assertEquals(order, updateOrder);
    }

}
