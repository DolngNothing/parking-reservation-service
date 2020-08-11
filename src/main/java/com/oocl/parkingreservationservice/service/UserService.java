package com.oocl.parkingreservationservice.service;

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


    public User login(String phone, String password) {
        return userRepository.findByPhone(phone);
    }
}
