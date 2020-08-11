package com.oocl.parkingreservationservice.service;


import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.IllegalOrderOperationException;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;
import com.oocl.parkingreservationservice.exception.ParkingOrderException;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import com.oocl.parkingreservationservice.utils.RegexUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.oocl.parkingreservationservice.constants.StatusContants.*;

@Service
public class ParkingOrderService {
    private static final String OVERDUE_MESSAGE = "时间段已过期，无法取消";
    private static final String NONE_EXISTENT_MESSAGE = "订单不存在";
    private static final String ALREADY_CANCEL_MESSAGE = "订单已取消，请勿重复操作";
    public static final double MILLISECONDSPERHOUR = 3600000.0;
    private final ParkingOrderRepository parkingOrderRepository;
    private final UserRepository userRepository;
    private final ParkingLotRepository parkingLotRepository;

    public ParkingOrderService(ParkingOrderRepository parkingOrderRepository, UserRepository userRepository,
                               ParkingLotRepository parkingLotRepository) {
        this.parkingOrderRepository = parkingOrderRepository;
        this.userRepository = userRepository;
        this.parkingLotRepository = parkingLotRepository;

    }

    public ParkingOrder getOrderById(Integer orderId) throws ParkingOrderException {
        ParkingOrder parkingOrder = parkingOrderRepository.findById(orderId).orElse(null);
        if (parkingOrder == null) {
            throw new ParkingOrderException(NONE_EXISTENT_MESSAGE);
        }
        return parkingOrder;
    }

    public ParkingOrderResponse cancelOrder(Integer orderId) throws ParkingOrderException, ParseException {
        ParkingOrder order = getOrderById(orderId);
        switch (order.getStatus()) {
            case WAIT_FOR_SURE:
                order.setStatus(DELETED);
                return ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(parkingOrderRepository.save(order));
            case ALREADY_SURE:
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                Date date1 = format.parse(order.getParkingStartTime());
                if (date.compareTo(date1) <= 0) {
                    order.setStatus(DELETED);
                    return ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(parkingOrderRepository.save(order));
                } else {
                    throw new ParkingOrderException(OVERDUE_MESSAGE);
                }
            case DELETED:
                throw new ParkingOrderException(ALREADY_CANCEL_MESSAGE);
            default:
                break;
        }
        return ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(order);
    }

    public ParkingOrderResponse confirmParkingOrder(Integer orderId) throws IllegalOrderOperationException, OrderNotExistException {
        ParkingOrder parkingOrder = parkingOrderRepository.findById(orderId).orElse(null);
        if (parkingOrder == null) {
            throw new OrderNotExistException();
        }
        if (parkingOrder.getStatus().equals(StatusContants.ALREADY_SURE)) {
            throw new IllegalOrderOperationException(MessageConstants.ODER_CONFIRMED);
        } else if (parkingOrder.getStatus().equals(StatusContants.DELETED)) {
            throw new IllegalOrderOperationException(MessageConstants.ODER_CANCELED);
        }
        parkingOrder.setStatus(StatusContants.ALREADY_SURE);
        parkingOrderRepository.save(parkingOrder);
        return ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(parkingOrder);

    }

    public ParkingOrderResponse addParkingOrder(ParkingOrder parkingOrder, String phone, String email) throws IllegalParameterException {
        if (!RegexUtils.validateMobilePhone(phone))
            throw new IllegalParameterException("预约失败，手机格式不正确");
        if (!RegexUtils.checkPlateNumberFormat(parkingOrder.getCarNumber()))
            throw new IllegalParameterException("预约失败，车牌格式不正确");
        if (!RegexUtils.validateEmail(email))
            throw new IllegalParameterException("预约失败，邮箱格式不正确");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double price;
        Optional<ParkingLot> parkingOrderOptional = parkingLotRepository.findById(parkingOrder.getParkingLotId());
        if (!parkingOrderOptional.isPresent()) throw new IllegalParameterException("不存在该停车场");
        double pricePerHour = parkingOrderOptional.get().getPrice();
        try {
            Date startTime = format.parse(parkingOrder.getParkingStartTime());
            Date endTime = format.parse(parkingOrder.getParkingEndTime());
            if (startTime.after(endTime)) throw new IllegalParameterException("预约失败，时间段已过期");
            if (startTime.before(new Date())) throw new IllegalParameterException("预约失败，时间段已过期");
            if (endTime.before(new Date())) throw new IllegalParameterException("预约失败，时间段已过期");
            price = (endTime.getTime() - startTime.getTime()) / MILLISECONDSPERHOUR * pricePerHour;
        } catch (ParseException | IllegalParameterException e) {
            throw new IllegalParameterException("预约失败，时间段已过期");
        }
        User user = userRepository.findFirstByEmail(email);

        parkingOrder.setUserId(user.getId());
        parkingOrder.setPrice(price);
        parkingOrder.setStatus(WAIT_FOR_SURE);
        ParkingOrder returnParkingOrder = parkingOrderRepository.save(parkingOrder);
        ParkingOrderResponse parkingOrderResponse = ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(returnParkingOrder);
        parkingOrderResponse.setParkingLotName(parkingOrderOptional.get().getName());
        parkingOrderResponse.setLocation(parkingOrderOptional.get().getLocation());
        return parkingOrderResponse;
    }

}
