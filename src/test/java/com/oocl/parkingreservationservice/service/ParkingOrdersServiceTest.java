package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ParkingOrdersServiceTest {
    @Test
    void should_return_confirm_parking_order_when_confirm_order_given_order_id() {
//        given
        Integer orderId = 1;
        Integer ODER_CONFIRMED = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId,1,1,"2020-8-10 12:25:30","2020-8-10 14:25:30",0,"1234");
        ParkingOrderResponse parkingOrderResponse;
        ParkingOrderRepository parkingOrderRepository = mock(ParkingOrderRepository.class);
        given(parkingOrderRepository.findById(orderId).orElse(null)).willReturn(parkingOrder);
        ParkingOrderService parkingOrderService = new ParkingOrderService(parkingOrderRepository);
//        when
        parkingOrderResponse =  parkingOrderService.confirmParkingOrder(orderId);
//        then
        assertEquals(ODER_CONFIRMED,parkingOrderResponse.getStatus());
    }
}
