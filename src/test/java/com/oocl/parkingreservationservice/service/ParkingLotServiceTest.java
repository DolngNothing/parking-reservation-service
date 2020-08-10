package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.service.impl.ParkingLotServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ParkingLotServiceTest {
    @Test
    public void should_return_parking_lot_list_when_get_parking_lot_given_x_and_y() {
        //given
        double longitude = 0;
        double latitude = 0;
        ParkingLotRepository parkingLotRepository = mock(ParkingLotRepository.class);
        List<ParkingLot> parkingLots = new ArrayList<>(Arrays.asList(new ParkingLot(), new ParkingLot()));
        given(parkingLotRepository.findAll()).willReturn(parkingLots);
        ParkingLotService parkingLotService = new ParkingLotServiceImpl(parkingLotRepository);
        //when
        List<ParkingLot> parkingLotsSaved = parkingLotService.getParkingLots(longitude, latitude);
        //then
        assertEquals(0, parkingLotsSaved.size());
    }
}
