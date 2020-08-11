package com.oocl.parkingreservationservice.mapper;

import com.oocl.parkingreservationservice.dto.UserLoginResponse;
import com.oocl.parkingreservationservice.model.User;
import org.springframework.beans.BeanUtils;

public class UserMapper {
    public static UserLoginResponse convertToUserLoginResponse(User user) {
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        BeanUtils.copyProperties(user, userLoginResponse);
        return userLoginResponse;
    }
}
