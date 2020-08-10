package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.service.impl.ParkingLotServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {
    @Test
    void should_return_illegal_argument_exception_when_get_parking_lot_given_null() {
        //given
        ParkingLotRepository parkingLotRepository = mock(ParkingLotRepository.class);
        ParkingLotService parkingLotService = new ParkingLotServiceImpl(parkingLotRepository);
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> parkingLotService.getParkingLots(null, null));
        //then
        assertEquals(IllegalArgumentException.class, exception.getClass());
    }
}
