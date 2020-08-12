package com.oocl.parkingreservationservice.integration;

import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mockMvc;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        userList = new ArrayList<>();
        userList.add(userRepository.save(new User(null, "13427560238", "1610692147@qq.com", "spike", "123")));
        userList.add(userRepository.save(new User(null, "13427560237", "1610692147@qq.com", "spike", "123")));
        userList.add(userRepository.save(new User(null, "13427560239", "1610692147@qq.com", "spike", "123")));

    }


    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void should_return_user_login_response_when_login_given_right_phone_and_right_password() throws Exception {
        //given
        User user = userList.get(0);
        String userLoginRequest = "{\n" +
                "    \"phoneNumber\":\"13427560238\",\n" +
                "    \"password\":\"123\"\n" +
                "}";
        //when
        mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(userLoginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));

        //then
    }

    @Test
    void should_return_wrong_message_when_login_given_right_phone_and_wrong_password() throws Exception {
        //given
        User user = userList.get(0);
        String userLoginInfo = "{\n" +
                "    \"phoneNumber\":\"13427560238\",\n" +
                "    \"password\":\"1\"\n" +
                "}";

        //when
        mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(userLoginInfo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(MessageConstants.WRONG_PASSWORD));
    }

    @Test
    void should_return_not_exist_user_message_when_login_given_not_exist_phone_and_wrong_password() throws Exception {
        //given
        User user = userList.get(0);
        String userLoginInfo = "{\n" +
                "    \"phoneNumber\":\"13427560249\",\n" +
                "    \"password\":\"123\"\n" +
                "}";

        //when
        mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(userLoginInfo))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(MessageConstants.USER_NOT_EXIST));
    }
}
