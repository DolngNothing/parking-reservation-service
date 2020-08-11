package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class UserServiceTest {

    UserService userService;
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void should_return_useId_and_username_when_login_given_user() {
        //given
        String phone = "13427560238";
        String password = "123";
        User user = new User(1, "13427560238", "1610692147@qq.com", "spike", "123");
        given(userRepository.findByPhone(phone)).willReturn(user);

        //when
        User loginedUser = userService.login(phone, password);

        //then
        assertEquals(user.getId(), loginedUser.getId());
        assertEquals(user.getUsername(), loginedUser.getUsername());
        assertEquals(user.getPhoneNumber(), loginedUser.getPhoneNumber());
        assertEquals(user.getPassword(), loginedUser.getPassword());
        assertEquals(user.getEmail(), loginedUser.getEmail());
    }
}
