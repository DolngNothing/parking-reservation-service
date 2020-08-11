package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService() {
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User login(String phone, String password) throws IllegalParameterException {
        User user = userRepository.findByPhone(phone);
        if (!password.equals(user.getPassword())) {
            throw new IllegalParameterException(MessageConstants.WRONG_PASSWORD);
        }

        return user;
    }
}
