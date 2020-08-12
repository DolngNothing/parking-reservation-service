package com.oocl.parkingreservationservice.controller;

import com.oocl.parkingreservationservice.dto.UserLoginRequest;
import com.oocl.parkingreservationservice.dto.UserLoginResponse;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.exception.UserNotExistException;
import com.oocl.parkingreservationservice.mapper.UserMapper;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginRequest userLoginRequest) throws IllegalParameterException, UserNotExistException {
        User user = userService.login(userLoginRequest.getPhoneNumber(), userLoginRequest.getPassword());
        return UserMapper.convertToUserLoginResponse(user);
    }
}
