package com.oocl.parkingreservationservice.integration;

import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mockMvc;
    List<User> userList;

    @BeforeEach
    void setUp() {
        List<User> users = new ArrayList<>();
        users.add(new User(1,"13427560238","1610692147@qq.com","spike","123"));
        users.add(new User(2,"13427560239","1610692149@qq.com","james","456"));
        users.add(new User(3, "13427560237", "1610692148@qq.com", "penny", "789"));
        userList = userRepository.saveAll(users);
    }


    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

//    @Test
//    void should_return_userid_and_username_when_login_given_phone_and_password() throws Exception {
//        //given
//        User user = userList.get(0);
//        String userLoginInfo = "{\n" +
//                "    \"phoneNumber\":\"13427560238\",\n" +
//                "    \"password\":\"123\"\n" +
//                "}";
//
//        //when
//        mockMvc.perform(post(("/user/login")).contentType(MediaType.APPLICATION_JSON).content(userLoginInfo))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.username").value(user.getUsername()));
//    }
}
