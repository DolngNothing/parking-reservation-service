package com.oocl.parkingreservationservice.service;


import com.oocl.parkingreservationservice.exception.ParkingOrderException;

import com.oocl.parkingreservationservice.exception.IllegalParameterException;

import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.utils.RegexUtils;
import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.oocl.parkingreservationservice.constants.StatusContants.*;

@Service
public class ParkingOrderService {
    private static final String OVERDUE_MESSAGE = "时间段已过期，无法取消";
    private static final String NONE_EXISTENT_MESSAGE = "订单不存在";
    private static final String ALREADY_CANCEL_MESSAGE = "订单已取消，请勿重复操作";
    private final ParkingOrderRepository parkingOrderRepository;

    public ParkingOrderService(ParkingOrderRepository parkingOrderRepository) {
        this.parkingOrderRepository =parkingOrderRepository;
    }

    public ParkingOrder getOrderById(Integer orderId) throws ParkingOrderException {
        ParkingOrder parkingOrder = parkingOrderRepository.findById(orderId).orElse(null);
        if(parkingOrder == null){
            throw new ParkingOrderException(NONE_EXISTENT_MESSAGE);
        }
        return parkingOrder;
    }

    public ParkingOrder cancelOrder(Integer orderId) throws ParkingOrderException, ParseException {
        ParkingOrder order = getOrderById(orderId);
        switch (order.getStatus()) {
            case WAIT_FOR_SURE:
                order.setStatus(DELETED);
                return parkingOrderRepository.save(order);
            case ALREADY_SURE:
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                Date date1 = format.parse(order.getParkingStartTime());
                if (date.compareTo(date1) <= 0) {
                    order.setStatus(DELETED);
                    return parkingOrderRepository.save(order);
                } else {
                    throw new ParkingOrderException(OVERDUE_MESSAGE);
                }
            case DELETED:
                throw new ParkingOrderException(ALREADY_CANCEL_MESSAGE);
        }
        return order;
    }

    public ParkingOrderResponse confirmParkingOrder(Integer orderId) {
        ParkingOrder parkingOrder = parkingOrderRepository.findById(orderId).orElse(null);
        assert parkingOrder != null;
        parkingOrder.setStatus(StatusContants.ALREADY_SURE);
        parkingOrderRepository.save(parkingOrder);
        return  ParkingOrderMapper.converToParkingOrderResponse(parkingOrder);

    }

    public ParkingOrder addParkingOrder(ParkingOrder parkingOrder, String phone, String email) throws IllegalParameterException {
        if(!RegexUtils.validateMobilePhone(phone))
            throw new IllegalParameterException();
        if(!RegexUtils.checkPlateNumberFormat(parkingOrder.getCarNumber()))
            throw new IllegalParameterException();
        if(!RegexUtils.validateEmail(email))
            throw new IllegalParameterException();
        return null;
    }

}
