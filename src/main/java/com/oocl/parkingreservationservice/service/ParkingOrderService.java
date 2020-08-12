package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.exception.*;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import com.oocl.parkingreservationservice.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.oocl.parkingreservationservice.constants.StatusContants.*;

@Service
public class ParkingOrderService {
    public static final double MILLISECONDSPERHOUR = 3600000.0;
    private static final String OVERDUE_MESSAGE = "时间段已过期，无法取消";
    private static final String NONE_EXISTENT_MESSAGE = "订单不存在";
    private static final String ALREADY_CANCEL_MESSAGE = "订单已取消，请勿重复操作";
    private final ParkingOrderRepository parkingOrderRepository;
    private final UserRepository userRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Autowired
    private UserService userService;
    @Autowired
    RabbitMqService rabbitMqService;

    public ParkingOrderService(ParkingOrderRepository parkingOrderRepository, UserRepository userRepository,
                               ParkingLotRepository parkingLotRepository) {
        this.parkingOrderRepository = parkingOrderRepository;
        this.userRepository = userRepository;
        this.parkingLotRepository = parkingLotRepository;

    }

    public ParkingOrderResponse getOrderById(Integer orderId) throws ParkingOrderException {
        ParkingOrder parkingOrder = parkingOrderRepository.findById(orderId).orElse(null);
        ParkingOrderResponse parkingOrderResponse = ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(parkingOrder);
        if (parkingOrder == null) {
            throw new ParkingOrderException(NONE_EXISTENT_MESSAGE);
        }
        return parkingOrderResponse;
    }

    public ParkingOrderResponse cancelOrder(Integer orderId) throws ParkingOrderException, ParseException {
        ParkingOrder order = parkingOrderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new ParkingOrderException(NONE_EXISTENT_MESSAGE);
        }
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
        if (rabbitMqService!=null){
            rabbitMqService.sendMessageToRabbitMq(parkingOrder);
        }
        return ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(parkingOrder);
    }

    public ParkingOrderResponse addParkingOrder(ParkingOrder parkingOrder, String phone, String email) throws IllegalParameterException, UserNotExistException {
        if (!RegexUtils.validateMobilePhone(phone)) {
            throw new IllegalParameterException("预约失败，手机格式不正确");
        }
        if (!RegexUtils.checkPlateNumberFormat(parkingOrder.getCarNumber())) {
            throw new IllegalParameterException("预约失败，车牌格式不正确");
        }
        if (!RegexUtils.validateEmail(email)) {
            throw new IllegalParameterException("预约失败，邮箱格式不正确");
        }

        double price;
        Optional<ParkingLot> parkingOrderOptional = parkingLotRepository.findById(parkingOrder.getParkingLotId());
        if (!parkingOrderOptional.isPresent()) {
            throw new IllegalParameterException("不存在该停车场");
        }
        double pricePerHour = parkingOrderOptional.get().getPrice();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = new Date(Long.parseLong(parkingOrder.getParkingStartTime()));
            Date endTime = new Date(Long.parseLong(parkingOrder.getParkingEndTime()));
            parkingOrder.setParkingStartTime(format.format(startTime));
            parkingOrder.setParkingEndTime(format.format(endTime));
            if (startTime.after(endTime)) {
                throw new IllegalParameterException("预约失败，时间段已过期");
            }
            if (startTime.before(new Date())) {
                throw new IllegalParameterException("预约失败，时间段已过期");
            }
            if (endTime.before(new Date())) {
                throw new IllegalParameterException("预约失败，时间段已过期");
            }
            price = (endTime.getTime() - startTime.getTime()) / MILLISECONDSPERHOUR * pricePerHour;
        } catch (IllegalParameterException e) {
            throw new IllegalParameterException("预约失败，时间段已过期");
        }
        //ToDO:phone
        User user = userRepository.findByPhone(phone);
//        User user = userRepository.findFirstByEmail(email);
///ToDo:加空用户判断
        if(user == null)
            throw new UserNotExistException(MessageConstants.USER_NOT_EXIST);
        parkingOrder.setUserId(user.getId());
        parkingOrder.setPrice(price);
        parkingOrder.setStatus(WAIT_FOR_SURE);
        ParkingOrder returnParkingOrder = parkingOrderRepository.save(parkingOrder);
        ParkingOrderResponse parkingOrderResponse = ParkingOrderMapper.convertParkingOrderToParkingOrderResponse(returnParkingOrder);
        parkingOrderResponse.setParkingLotName(parkingOrderOptional.get().getName());
        parkingOrderResponse.setLocation(parkingOrderOptional.get().getLocation());
        parkingOrderResponse.setPhoneNumber(phone);
        parkingOrderResponse.setEmail(email);
        //ToDo:
        return parkingOrderResponse;
    }


    public List<ParkingOrderResponse> getAllOrdersByEmail(String email) throws InquiryOrderException {
        User userByEmail = userRepository.findByEmail(email);
        if(userByEmail == null){
            throw new InquiryOrderException("指定email不存在，请输入正确的email");
        }
        List<ParkingOrder> parkingOrders = parkingOrderRepository.findAllByUserId(userByEmail.getId());
        return parkingOrders.stream().map(ParkingOrderMapper::convertParkingOrderToParkingOrderResponse).collect(Collectors.toList());
    }

    public List<ParkingOrderResponse> getAllOrdersByPhoneNumber(String phoneNumber) throws InquiryOrderException {
        User userByPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
        if(userByPhoneNumber == null){
            throw new InquiryOrderException("指定phoneNumber不存在，请输入正确的phoneNumber");
        }
        List<ParkingOrder> parkingOrders = parkingOrderRepository.findAllByUserId(userByPhoneNumber.getId());
        return parkingOrders.stream().map(ParkingOrderMapper::convertParkingOrderToParkingOrderResponse).collect(Collectors.toList());
    }

    public List<ParkingOrderResponse> getAllOrdersByUserId(Integer id) throws InquiryOrderException {
        User userByUserId = userRepository.findById(id).orElse(null);
        if(userByUserId == null){
            throw new InquiryOrderException("指定userID不存在，请输入正确的userID");
        }
        List<ParkingOrder> parkingOrders = parkingOrderRepository.findAllByUserId(id);
        return parkingOrders.stream().map(ParkingOrderMapper::convertParkingOrderToParkingOrderResponse).collect(Collectors.toList());
    }
}

