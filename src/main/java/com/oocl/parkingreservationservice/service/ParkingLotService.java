package com.oocl.parkingreservationservice.service;

import com.alibaba.fastjson.JSON;
import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.dto.ParkingLotResponse;
import com.oocl.parkingreservationservice.mapper.ParkingLotMapper;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XUAL7
 */
@Service
public class ParkingLotService {

    public static final String NO_LONGITUDE = "经度缺失";
    public static final String NO_LATITUDE = "纬度缺失";
    public static final String PARKING_LOTS = "parkingLots";
    private static final double EARTH_RADIUS = 6378137;
    public static final int NEARBY_DISTANCE = 2000;
    private final ParkingLotRepository parkingLotRepository;
    private StringRedisTemplate redisTemplate;
    private CommentService commentService;

    @Autowired
    public ParkingLotService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<ParkingLotResponse> getParkingLots(Double longitude, Double latitude, String type) {
        Assert.notNull(longitude, NO_LONGITUDE);
        Assert.notNull(latitude, NO_LATITUDE);
        List<ParkingLot> parkingLots = filterParkingLots(longitude, latitude, type);
        List<ParkingLotResponse> parkingLotResponses = new ArrayList<>();
        for (ParkingLot parkingLot : parkingLots) {
            ParkingLotResponse parkingLotResponse = ParkingLotMapper.map(parkingLot);
            parkingLotResponse.setDistance(getDistance(longitude, latitude,
                    Double.parseDouble(parkingLot.getLongitude()), Double.parseDouble(parkingLot.getLatitude())));
            CommentResponse commentResponse = commentService.getAllComment(parkingLot.getId());
            parkingLotResponse.setAvgScore(commentResponse.getAvgScore());
            parkingLotResponses.add(parkingLotResponse);
        }
        return parkingLotResponses.stream().sorted(Comparator.comparing(parkingLotResponse -> sortParkingLot(
                getDistance(Double.parseDouble(parkingLotResponse.getLongitude()),
                        Double.parseDouble(parkingLotResponse.getLatitude()),
                        longitude, latitude),
                parkingLotResponse.getAvgScore(),
                parkingLotResponse.getPrice() == null ? 0 : parkingLotResponse.getPrice()))).collect(Collectors.toList());
    }

    public Double sortParkingLot(double distance, double score, double price) {
        return -(0.5 * (1 - distance / 2000.0) + 0.25 * (score / 5.0) + 0.25 * (1 - price / 100.0));
    }

    public List<ParkingLot> filterParkingLots(Double longitude, Double latitude, String type) {
        return getParkingLots().stream()
                .filter(parkingLot -> parkingLot.getLatitude() != null && parkingLot.getLongitude() != null)
                .filter(parkingLot -> getDistance(
                        Double.parseDouble(parkingLot.getLongitude()), Double.parseDouble(parkingLot.getLatitude()),
                        longitude, latitude) <= NEARBY_DISTANCE)
                .filter(parkingLot -> type == null || parkingLot.getType().equals(type)).limit(5)
                .collect(Collectors.toList());
    }


    public List<ParkingLot> getParkingLots() {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        List<ParkingLot> parkingLots = null;
        if (redisTemplate.getKeySerializer() != null && redisTemplate.hasKey(PARKING_LOTS)) {
            parkingLots = JSON.parseArray(operations.get(PARKING_LOTS), ParkingLot.class);
        }
        if (parkingLots == null || parkingLots.isEmpty()) {
            parkingLots = parkingLotRepository.findAll();
            if (operations != null) {
                operations.set(PARKING_LOTS, JSON.toJSONString(parkingLots));
            }
        }
        return parkingLots;
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
