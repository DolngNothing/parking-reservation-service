package com.oocl.parkingreservationservice.utils;

import com.oocl.parkingreservationservice.dto.WebsocketRequest;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WebsocketService {

    private final HashMap<WebsocketRequest, String> hashMap = new HashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;


    public WebsocketService(ParkingLotRepository parkingLotRepository, ParkingOrderRepository parkingOrderRepository, SimpMessagingTemplate messagingTemplate) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingOrderRepository = parkingOrderRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToAllConnet(String msg) throws ParseException {

        String[] arr = msg.split("/");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long orderStartTime = sdf.parse(arr[1]).getTime();
        long orderEndTime = sdf.parse(arr[2]).getTime();
        msg = "/" + orderStartTime + "/" + orderEndTime + "/" + arr[3];
        String[] arr1 = msg.split("/");

        WebsocketRequest request = new WebsocketRequest(Long.parseLong(arr1[1]), Long.parseLong(arr1[2]), Integer.parseInt(arr1[3]));
        //注销
        hashMap.remove(request);

        List<ParkingOrder> allByParkingLotId = parkingOrderRepository.findAllByParkingLotId(request.getParkingLotId());
        List<ParkingOrder> parkingOrders = allByParkingLotId.stream().filter(ParkingOrder::isSure).collect(Collectors.toList());

        System.out.println("预约成功时看看对应订单数量：" + parkingOrders.size());
        ParkingLot parkingLot = parkingLotRepository.findById(request.getParkingLotId()).orElse(null);
        //遍历寻找广播:
        Set<Map.Entry<WebsocketRequest, String>> set = hashMap.entrySet();
        for (Map.Entry<WebsocketRequest, String> e : set) {
            if (isTimeSame(e, request)) {
                assert parkingLot != null;
                Integer count = count(e.getKey(), parkingOrders, parkingLot.getCapacity());
                System.out.println("save send: " + e.getValue() + "count: " + count);
                messagingTemplate.convertAndSend("/topic" + e.getValue(), count);
            }
        }
    }


    public void sentBack(String msg) throws ParseException {  //msg->客户端传来的消息
        String[] arr = msg.split("/");
        WebsocketRequest request = new WebsocketRequest(Long.parseLong(arr[1]), Long.parseLong(arr[2]), Integer.parseInt(arr[3]));
        //注册
        hashMap.put(request, msg);

        //根据时间车位拿到空位信息
        List<ParkingOrder> allByParkingLotId = parkingOrderRepository.findAllByParkingLotId(request.getParkingLotId());
        List<ParkingOrder> parkingOrders = allByParkingLotId.stream().filter(ParkingOrder::isSure).collect(Collectors.toList());
        ParkingLot parkingLot = parkingLotRepository.findById(request.getParkingLotId()).orElse(null);
        assert parkingLot != null;
        Integer count = count(request, parkingOrders, parkingLot.getCapacity());
        System.out.println("register send: " + msg + "count: " + count);
        messagingTemplate.convertAndSend("/topic" + msg, count);
    }

    public Boolean isTimeSame(Map.Entry<WebsocketRequest, String> e, WebsocketRequest request) {
        if (e.getKey().getParkingLotId().equals(request.getParkingLotId())) {
            return request.getEndTime() >= e.getKey().getStartTime() && request.getStartTime() <= e.getKey().getEndTime();
        } else {
            return false;
        }
    }

    public Integer count(WebsocketRequest websocketRequest, List<ParkingOrder> allByParkingLotId, Integer capacity) throws ParseException {
        Integer count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (allByParkingLotId != null && allByParkingLotId.size() > 0) {
            for (ParkingOrder parkingOrder : allByParkingLotId) {
                long orderStartTime = sdf.parse(parkingOrder.getParkingStartTime()).getTime();
                long orderEndTime = sdf.parse(parkingOrder.getParkingEndTime()).getTime();
                if (websocketRequest.getEndTime() < orderStartTime || websocketRequest.getStartTime() > orderEndTime) {
                    continue;
                }
                count++;
            }
            return capacity - count;
        }
        return capacity;
    }
}
