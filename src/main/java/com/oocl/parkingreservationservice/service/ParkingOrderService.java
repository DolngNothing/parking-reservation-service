package com.oocl.parkingreservationservice.service;


import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.exception.ParkingOrderException;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import com.oocl.parkingreservationservice.utils.RegexUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.exception.IllegalOrderOperationException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;

import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
import org.springframework.stereotype.Service;


import static com.oocl.parkingreservationservice.constants.StatusContants.*;

@Service
public class ParkingOrderService {
    private static final String OVERDUE_MESSAGE = "时间段已过期，无法取消";
    private static final String NONE_EXISTENT_MESSAGE = "订单不存在";
    private static final String ALREADY_CANCEL_MESSAGE = "订单已取消，请勿重复操作";
    private final ParkingOrderRepository parkingOrderRepository;
    private final UserRepository userRepository;

    public ParkingOrderService(ParkingOrderRepository parkingOrderRepository,UserRepository userRepository) {
        this.parkingOrderRepository = parkingOrderRepository;
        this.userRepository=userRepository;

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

    public ParkingOrderResponse confirmParkingOrder(Integer orderId) throws IllegalOrderOperationException, OrderNotExistException {

        Optional<ParkingOrder> parkingOrderOptional = parkingOrderRepository.findById(orderId);

        if (parkingOrderOptional == null){
            throw new OrderNotExistException();
        }
        ParkingOrder parkingOrder = parkingOrderOptional.orElse(null);
        assert parkingOrder != null;
        if (parkingOrder.getStatus().equals(StatusContants.ALREADY_SURE)){
            throw new IllegalOrderOperationException(MessageConstants.ODER_CONFIRMED);
        }else if (parkingOrder.getStatus().equals(StatusContants.DELETED)){
            throw new IllegalOrderOperationException(MessageConstants.ODER_CANCELED);
        }
        parkingOrder.setStatus(StatusContants.ALREADY_SURE);
        parkingOrderRepository.save(parkingOrder);
        return  ParkingOrderMapper.converToParkingOrderResponse(parkingOrder);

    }
    public ParkingOrder addParkingOrder(ParkingOrder parkingOrder, String phone, String email) throws IllegalParameterException {
        if (!RegexUtils.validateMobilePhone(phone))
            throw new IllegalParameterException("预约失败，手机格式不正确");
        if (!RegexUtils.checkPlateNumberFormat(parkingOrder.getCarNumber()))
            throw new IllegalParameterException("预约失败，车牌格式不正确");
        if (!RegexUtils.validateEmail(email))
            throw new IllegalParameterException("预约失败，邮箱格式不正确");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startTime = format.parse(parkingOrder.getParkingStartTime());
            Date endTime = format.parse(parkingOrder.getParkingEndTime());
            if(startTime.after(endTime)) throw new IllegalParameterException("预约失败，时间段已过期");
            if(startTime.before(new Date()))throw new IllegalParameterException("预约失败，时间段已过期");
            if(endTime.before(new Date())) throw new IllegalParameterException("预约失败，时间段已过期");
        } catch (ParseException | IllegalParameterException e) {
            e.printStackTrace();
            throw new IllegalParameterException("预约失败，时间段已过期");
        }
        User user=userRepository.findFirst1ByEmail(email).get(0);
        parkingOrder.setUserId(user.getId());
        return parkingOrderRepository.save(parkingOrder);
    }

}
