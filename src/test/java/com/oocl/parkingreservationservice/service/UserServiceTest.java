package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.exception.IllegalParameterException;
import com.oocl.parkingreservationservice.exception.UserNotExistException;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void should_return_useId_and_username_when_login_given_user() throws IllegalParameterException, UserNotExistException {
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


    @Test
    void should_throw_wrong_password_exception_and_return_wrong_password_message_when_login_given_right_phone_wrong_password() {
        //given
        String phone = "13427560238";
        String password = "456";
        User user = new User(1, "13427560238", "1610692147@qq.com", "spike", "123");
        given(userRepository.findByPhone(phone)).willReturn(user);

        //when
        Exception exception = assertThrows(IllegalParameterException.class, () -> userService.login(phone, password));

        //then
        assertEquals(IllegalParameterException.class, exception.getClass());
        assertEquals(MessageConstants.WRONG_PASSWORD, exception.getMessage());
    }


    @Test
    void should_throw_not_exist_user_exception_and_return_not_exist_user_message_when_login_given_not_exist_phone_and_wrong_password() {
        //given
        String phone = "1342756023";
        String password = "456";
        given(userRepository.findByPhone(phone)).willReturn(null);

        //when
        Exception exception = assertThrows(UserNotExistException.class, () -> userService.login(phone, password));

        //then
        assertEquals(UserNotExistException.class, exception.getClass());
        assertEquals(MessageConstants.USER_NOT_EXIST, exception.getMessage());
    }
}
