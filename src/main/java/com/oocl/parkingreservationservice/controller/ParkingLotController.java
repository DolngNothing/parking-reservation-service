package com.oocl.parkingreservationservice.controller;


import com.oocl.parkingreservationservice.dto.ParkingLotResponse;
import com.oocl.parkingreservationservice.exception.NotLoginException;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author XUAL7
 */
@RestController
@RequestMapping("/parkingLots")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @Autowired
    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @GetMapping(params = {"lng", "lat"})
    public List<ParkingLotResponse> getParkingLots(Double lng, Double lat, HttpServletRequest httpServletRequest) throws NotLoginException {
        return this.parkingLotService.getParkingLots(lng, lat);
    }

}
