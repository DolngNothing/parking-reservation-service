package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.config.DirectRabbitConfig;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMqService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ParkingLotRepository parkingLotRepository;

    public void sendMessageToRabbitMq(ParkingOrder parkingOrder) {
        Map<String, String> message = new HashMap<>();
        User user = userRepository.findById(parkingOrder.getUserId()).orElse(null);
        ParkingLot parkingLot = parkingLotRepository.findById(parkingOrder.getParkingLotId()).orElse(null);
        if(user != null && parkingLot != null){
            message.put("phoneNumber", user.getPhoneNumber());
            message.put("username", user.getUsername());
            message.put("parkingName", parkingLot.getName());
            message.put("parkingOrderId", String.valueOf(parkingOrder.getId()));
            rabbitTemplate.convertAndSend(DirectRabbitConfig.EXCHANGE_NAME,  DirectRabbitConfig.ROUTING_KEY, message);
        }
    }
}
