package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.dto.ParkingLotResponse;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
        StringRedisTemplate stringRedisTemplate = mock(StringRedisTemplate.class);
        CommentService commentService = mock(CommentService.class);
        given(commentService.getAllComment(anyInt())).willReturn(new CommentResponse());
        given(stringRedisTemplate.hasKey(anyString())).willReturn(false);
        given(stringRedisTemplate.opsForValue()).willReturn(null);
        given(parkingLotRepository.findAll()).willReturn(parkingLots);
        ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository);
        parkingLotService.setRedisTemplate(stringRedisTemplate);
        //when
        List<ParkingLotResponse> parkingLotsSaved = parkingLotService.getParkingLots(longitude, latitude, null);
        //then
        assertEquals(0, parkingLotsSaved.size());
    }
}
