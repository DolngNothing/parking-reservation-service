package com.oocl.parkingreservationservice.controller;

import com.oocl.parkingreservationservice.dto.UserLoginRequest;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.exception.UserNotExistException;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    public static final String USER_NAME = "userName";
    public static final String USER_PHONE = "userPhone";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserLoginRequest userLoginRequest
            , HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IllegalParameterException, UserNotExistException {
        User user = userService.login(userLoginRequest.getPhoneNumber(), userLoginRequest.getPassword());
        httpServletRequest.getSession().setAttribute(USER_NAME, user.getUsername());
        httpServletRequest.getSession().setAttribute(USER_PHONE, user.getPhoneNumber());

        Map<String, String> userMap = new HashMap<>(4);
        userMap.put("id", String.valueOf(user.getId()));
        userMap.put("username", user.getUsername());
        userMap.put("session", httpServletRequest.getSession().getId());
        return userMap;
    }
}
