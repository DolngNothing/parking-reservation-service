package com.oocl.parkingreservationservice.service.impl;

import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.service.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XUAL7
 */
@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    public static final String NO_LONGITUDE = "经度缺失";
    public static final String NO_LATITUDE = "纬度缺失";
    private final ParkingLotRepository parkingLotRepository;
    private static final double EARTH_RADIUS = 6378137;

    @Autowired
    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    @Override
    public List<ParkingLot> getParkingLots(Double longitude, Double latitude) {
        Assert.notNull(longitude, NO_LONGITUDE);
        Assert.notNull(latitude, NO_LATITUDE);
        List<ParkingLot> parkingLots = getParkingLots();
        return parkingLots.stream()
                .filter(parkingLot -> parkingLot.getLatitude() != null && parkingLot.getLongitude() != null)
                .filter(parkingLot -> getDistance(
                        Double.parseDouble(parkingLot.getLongitude()), Double.parseDouble(parkingLot.getLatitude()),
                        longitude, latitude) <= 2000).collect(Collectors.toList());
    }

    public List<ParkingLot> getParkingLots() {
        return parkingLotRepository.findAll();
    }

    private double calculateRadian(double angle) {
        return angle * Math.PI / 180.0;
    }

    private double getDistance(Double longitudeX, Double latitudeX, Double longitudeY, Double latitudeY) {
        double radLatOfX = calculateRadian(latitudeX);
        double radLatOfY = calculateRadian(latitudeY);
        double radianOfLatitude = radLatOfX - radLatOfY;
        double radianOfLongitude = calculateRadian(longitudeX) - calculateRadian(longitudeY);
        double angle = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(radianOfLatitude / 2), 2) +
                Math.cos(radLatOfX) * Math.cos(radLatOfY) * Math.pow(Math.sin(radianOfLongitude / 2), 2)));
        return angle * EARTH_RADIUS;
    }
}
