package com.oocl.parkingreservationservice.utils;

import com.oocl.parkingreservationservice.utils.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RestController
public class WebsocketController {
    @Autowired
    private WebsocketService websocketService;


    @MessageMapping(value = "/websocket/save")
    /**
     * 确认一个订单后，霸占一个车位了，需要给所有涉及到的订单页面广播
     */
    public void sendAllConnect(String msg) throws ParseException {//msg->客户端传来的消息
        websocketService.sendToAllConnet(msg);
    }

    @MessageMapping(value = "/rege") //@注解2
    public void sendYourself(String msg) throws ParseException {
        System.out.println(msg);
        websocketService.sentBack(msg);
    }
}
