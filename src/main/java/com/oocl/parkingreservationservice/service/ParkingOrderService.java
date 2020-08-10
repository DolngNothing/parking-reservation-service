package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.utils.RegexUtils;

public class ParkingOrderService {
    public ParkingOrder addParkingOrder(ParkingOrder parkingOrder, String phone, String email) throws IllegalParameterException {
        if(!RegexUtils.validateMobilePhone(phone))
            throw new IllegalParameterException();
        if(!RegexUtils.checkPlateNumberFormat(parkingOrder.getCar_number()))
            throw new IllegalParameterException();
        return null;
    }
}
