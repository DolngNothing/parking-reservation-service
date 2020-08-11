package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.exception.UserNotExistException;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserService() {
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User login(String phone, String password) throws IllegalParameterException, UserNotExistException {
        User user = userRepository.findByPhone(phone);
        if(user == null){
            throw new UserNotExistException(MessageConstants.USER_NOT_EXIST);
        }
        if (!password.equals(user.getPassword())) {
            throw new IllegalParameterException(MessageConstants.WRONG_PASSWORD);
        }

        return user;
    }
}
