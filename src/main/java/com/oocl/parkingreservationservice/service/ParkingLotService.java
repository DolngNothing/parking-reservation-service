package com.oocl.parkingreservationservice.service;

import com.alibaba.fastjson.JSON;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XUAL7
 */
@Service
public class ParkingLotService {

    public static final String NO_LONGITUDE = "经度缺失";
    public static final String NO_LATITUDE = "纬度缺失";
    private static final double EARTH_RADIUS = 6378137;
    public static final String PARKING_LOTS = "parkingLots";
    private final ParkingLotRepository parkingLotRepository;
    private StringRedisTemplate redisTemplate;

    @Autowired
    public ParkingLotService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<ParkingLot> getParkingLots(Double longitude, Double latitude) {
        Assert.notNull(longitude, NO_LONGITUDE);
        Assert.notNull(latitude, NO_LATITUDE);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        List<ParkingLot> parkingLots = null;
        if (redisTemplate.getKeySerializer() != null && redisTemplate.hasKey(PARKING_LOTS)) {
            parkingLots = JSON.parseArray(operations.get(PARKING_LOTS), ParkingLot.class);
        }
        if (parkingLots == null || parkingLots.isEmpty()) {
            parkingLots = getParkingLots();
            if (operations != null) {
                operations.set(PARKING_LOTS, JSON.toJSONString(parkingLots));
            }
        }
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
