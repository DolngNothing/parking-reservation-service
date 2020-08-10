package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.model.ParkingLot;

import java.util.List;

/**
 * @author XUAL7
 */
public interface ParkingLotService {
    public List<ParkingLot> getParkingLots(Double longitude, Double latitude);
}
