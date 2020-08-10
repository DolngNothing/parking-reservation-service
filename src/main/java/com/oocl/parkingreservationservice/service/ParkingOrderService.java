package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.utils.RegexUtils;
import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.ParkingOrderResponse;
import com.oocl.parkingreservationservice.mapper.ParkingOrderMapper;
;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;

public class ParkingOrderService {
    private final ParkingOrderRepository parkingOrderRepository;
    public ParkingOrderService(ParkingOrderRepository parkingOrderRepository) {
        this.parkingOrderRepository =parkingOrderRepository;
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
        if(!RegexUtils.checkPlateNumberFormat(parkingOrder.getCar_number()))
            throw new IllegalParameterException();
        if(!RegexUtils.validateEmail(email))
            throw new IllegalParameterException();
        return null;
    }

}
