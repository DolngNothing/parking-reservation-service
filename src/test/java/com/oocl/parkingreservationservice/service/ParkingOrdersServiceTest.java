package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParkingOrdersServiceTest {
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
}
