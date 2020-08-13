package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.utils.WebsocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.text.ParseException;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;


public class WebsocketServiceTest {
    ParkingOrderRepository parkingOrderRepository;
    ParkingLotRepository parkingLotRepository;
    SimpMessagingTemplate messagingTemplate;
    WebsocketService websocketService;

    @BeforeEach
    void setUp() {
        parkingOrderRepository = Mockito.mock(ParkingOrderRepository.class);
        parkingLotRepository = Mockito.mock(ParkingLotRepository.class);
        messagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
        websocketService=new WebsocketService(parkingLotRepository,parkingOrderRepository,messagingTemplate);
    }

    @Test
    void should_return_void_when_sendToregister_given_Message() throws ParseException {
        String src="/1597240710579/1597417110579/215";
        given(parkingLotRepository.findById(215)).willReturn(Optional.of(new ParkingLot(215, null, null, null, 100, 20.0, null, null, null, "")));
        given(parkingOrderRepository.findAllByParkingLotId(215)).willReturn(Collections.singletonList(
                new ParkingOrder(1, null, "2020-08-12 23:50:03", "2020-08-14 23:50:03", 23, 215, null, "ALREADY_SURE", "粤C12345", 240.0)));
        websocketService.sentBack(src);
        Mockito.verify(parkingOrderRepository).findAllByParkingLotId(215);
    }

    @Test
    void should_return_void_when_sendMessage_given_Message() throws ParseException {
        String src="/2020-07-12 14:22:33/2020-07-13 14:22:33/215";
        given(parkingLotRepository.findById(215)).willReturn(Optional.of(new ParkingLot(215, null, null, null, 100, 20.0, null, null, null, "")));
        given(parkingOrderRepository.findAllByParkingLotId(215)).willReturn(Collections.singletonList(
                new ParkingOrder(1, null, "2020-08-12 23:50:03", "2020-08-14 23:50:03", 23, 215, null, "ALREADY_SURE", "粤C12345", 240.0)));
        websocketService.sendToAllConnet(src);
        Mockito.verify(parkingOrderRepository).findAllByParkingLotId(215);
    }
}
