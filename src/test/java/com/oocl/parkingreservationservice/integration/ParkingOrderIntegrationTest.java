package com.oocl.parkingreservationservice.integration;

import com.oocl.parkingreservationservice.constants.MessageConstants;
import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.controller.ParkingOrderController;
import com.oocl.parkingreservationservice.handler.GlobalExceptionHandler;
import com.oocl.parkingreservationservice.model.ParkingLot;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.model.User;
import com.oocl.parkingreservationservice.repository.ParkingLotRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import com.oocl.parkingreservationservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ParkingOrderIntegrationTest {

    @Autowired
    ParkingOrderRepository parkingOrderRepository;
    @Autowired
    ParkingLotRepository parkingLotRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ParkingOrderController parkingOrderController;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    MockMvc mockMvc;
    private int OrderId;
    private int parkLotId;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(parkingOrderController, globalExceptionHandler).build();
        ParkingOrder firstOrder = new ParkingOrder(1, null, "2020-08-10 12:25:30",
                "2020-08-10 14:25:30", -1, 1, "2020-08-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);
        ParkingOrder Order = parkingOrderRepository.save(firstOrder);
        OrderId = Order.getId();
        parkLotId = Order.getParkingLotId();
    }

    @AfterEach
    void tearDown() {
        parkingOrderRepository.deleteAll();
        parkingLotRepository.deleteAll();
        userRepository.deleteAll();
    }



    @Test
    void should_return_parking_order_when_hit_cancel_order_endpoint_given_certain_order_id() throws Exception {
        //when then
        mockMvc.perform(patch("/parkingOrders/" + OrderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parkingLotId").value(parkLotId))
                .andExpect(jsonPath("$.parkingStartTime").value("2020-08-10 12:25:30"))
                .andExpect(jsonPath("$.status").value(StatusContants.DELETED));
    }

    @Test
    void should_return_parking_order_response_when_hit_add_order_endpoint_given_info() throws Exception {
        ParkingLot parkingLot = new ParkingLot(null, "pakinglot1", "112.5", "22.3", 100, 0.5, "this is a test parkingLot", "asdasd", "");
        parkingLot = parkingLotRepository.save(parkingLot);
        User user = new User(null, "15920138471", "1214852999@qq.com", "James", "null");
        userRepository.save(user);
        String parkingStartTime = Long.toString(new Date().getTime() + 1000);
        String parkingEndTime = Long.toString(new Date().getTime() + 200000000);
        String orderInfo = "{\n" +
                "    \"email\":\"1214852999@qq.com\",\n" +
                "    \"phone\":\"15920138471\",\n" +
                "    \"parkingStartTime\":" + parkingStartTime + ",\n" +
                "    \"parkingEndTime\":" + parkingEndTime + ",\n" +
                "    \"parkingLotId\":" + parkingLot.getId() + ",\n" +
                "    \"carNumber\":\"浙A1063警\"\n" +
                "}";
        //when
        mockMvc.perform(post(("/parkingOrders")).contentType(MediaType.APPLICATION_JSON).content(orderInfo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.parkingLotId").isNumber());
        //then
    }

    @Test
    void should_return_bad_request_when_hit_add_order_endpoint_given_illegal_info() throws Exception {
        ParkingLot parkingLot = new ParkingLot(null, "pakinglot1", "112.5", "22.3", 100, 0.5, "this is a test parkingLot", "asdasd", "");
        parkingLot = parkingLotRepository.save(parkingLot);
        User user = new User(null, "15920138471", "1214852999@qq.com", "James", "null");
        userRepository.save(user);
        String parkingStartTime = Long.toString(new Date().getTime() + 1000);
        String parkingEndTime = Long.toString(new Date().getTime() + 2000);
        String orderInfo = "{\n" +
                "    \"email\":\"1214852999@q.com\",\n" +
                "    \"phone\":\"15920138471\",\n" +
                "    \"parkingStartTime\":" + parkingStartTime + ",\n" +
                "    \"parkingEndTime\":" + parkingEndTime + ",\n" +
                "    \"parkingLotId\":" + parkingLot.getId() + ",\n" +
                "    \"carNumber\":\"浙A1063\"\n" +
                '}';
        //when
        mockMvc.perform(post(("/parkingOrders")).contentType(MediaType.APPLICATION_JSON).content(orderInfo))
                .andExpect(status().isBadRequest());
        //then
    }

    @Test
    void should_return_order_when_hit_get_order_endpoint_given_order_id() throws Exception {
        //given
        ParkingOrder order = new ParkingOrder(1, null, "2020-08-10 12:25:30",
                "2020-08-10 14:25:30", 1, 1, "2020-08-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);
        ParkingOrder parkingOrder = parkingOrderRepository.save(order);

        //when
        mockMvc.perform(get("/parkingOrders/" + parkingOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(parkingOrder.getId()));
    }

    @Test
    void should_get_order_list_when_hit_get_order_endpoint_given_userId() throws Exception {
        //given
        User user = new User(1, "15626155019", null, "karen", "123");
        User user2 = new User(2, "15626155019", null, "karen", "123");
        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        List<ParkingOrder> parkingOrders = Arrays.asList(
                new ParkingOrder(1, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0),
                new ParkingOrder(2, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤B258警", 30.0),
                new ParkingOrder(3, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user2.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤C369警", 60.0)
        );
        parkingOrderRepository.save(parkingOrders.get(0));
        parkingOrderRepository.save(parkingOrders.get(1));
        parkingOrderRepository.save(parkingOrders.get(2));

        //when
        mockMvc.perform(get("/parkingOrders").param("userId", String.valueOf(user.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].carNumber").value("浙A1063警"))
                .andExpect(jsonPath("$[1].carNumber").value("粤B258警"));
    }

    @Test
    void should_get_order_list_when_hit_get_order_endpoint_given_email() throws Exception {
        //given
        String email = "545759585@qq.com";
        User user = new User(1, "15626155019", email, "karen", "123");
        User user2 = new User(2, "15626155019", "496349192@qq.com", "emon", "123");
        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        List<ParkingOrder> parkingOrders = Arrays.asList(
                new ParkingOrder(1, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0),
                new ParkingOrder(2, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤B258警", 30.0),
                new ParkingOrder(3, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user2.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤C369警", 60.0)
        );

        parkingOrderRepository.save(parkingOrders.get(0));
        parkingOrderRepository.save(parkingOrders.get(1));
        parkingOrderRepository.save(parkingOrders.get(2));

        //when
        mockMvc.perform(get("/parkingOrders").param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].carNumber").value("浙A1063警"))
                .andExpect(jsonPath("$[1].carNumber").value("粤B258警"));
    }

    @Test
    void should_get_order_list_when_hit_get_order_endpoint_given_phoneNumber() throws Exception {
        //given
        String phoneNumber = "15626155019";
        User user = new User(1, phoneNumber, null, "karen", "123");
        User user2 = new User(2, "13626155017", null, "emon", "123");
        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        List<ParkingOrder> parkingOrders = Arrays.asList(
                new ParkingOrder(1, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0),
                new ParkingOrder(2, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤B258警", 30.0),
                new ParkingOrder(3, null, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", user2.getId(), 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "粤C369警", 60.0)
        );
        parkingOrderRepository.save(parkingOrders.get(0));
        parkingOrderRepository.save(parkingOrders.get(1));
        parkingOrderRepository.save(parkingOrders.get(2));

        //when
        mockMvc.perform(get("/parkingOrders").param("phoneNumber", phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].carNumber").value("浙A1063警"))
                .andExpect(jsonPath("$[1].carNumber").value("粤B258警"));
    }


    @Test
    void should_return_order_already_confirm_message_when_confirm_a_order_given_order_id() throws Exception {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, null, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);
        ParkingOrder savedParkingOrder = parkingOrderRepository.save(parkingOrder);
//        when then
        mockMvc.perform(patch("/parkingOrders/" + savedParkingOrder.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("type", "comfirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusContants.ALREADY_SURE));
    }

    @Test
    void should_return_illegal_message_when_confirm_a_confirmed_order_given_order_id() throws Exception {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, null, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.ALREADY_SURE, "1234", 10.0);
        ParkingOrder savedParkingOrder = parkingOrderRepository.save(parkingOrder);
//        when then
        mockMvc.perform(patch("/parkingOrders/" + savedParkingOrder.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("type", "comfirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(MessageConstants.ODER_CONFIRMED));
    }

    @Test
    void should_return_illegal_message_when_confirm_a_canceled_order_given_order_id() throws Exception {
//        given
        Integer orderId = 1;
        ParkingOrder parkingOrder = new ParkingOrder(orderId, null, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.DELETED, "1234", 10.0);
        ParkingOrder savedParkingOrder = parkingOrderRepository.save(parkingOrder);
//        when then
        mockMvc.perform(patch("/parkingOrders/" + savedParkingOrder.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("type", "comfirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(MessageConstants.ODER_CANCELED));
    }

    @Test
    void should_return_not_exist_message_when_confirm_a_not_exist_order_given_order_id() throws Exception {
//        given
        int orderId = Integer.MAX_VALUE;
//        when then
        mockMvc.perform(patch("/parkingOrders/" + orderId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("type", "comfirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(MessageConstants.ODER_NOT_EXIST));
    }

//    @Test
//    void should_return_binary_when_hit_getOROrder_endpoint_given_order_id() throws Exception {
////        given
//        int orderId = 1;
//        String returnBinary = parkingOrderController.getQRCodeByOrderId(orderId);
////        when
//        ParkingOrder parkingOrder = new ParkingOrder(1, null, "2020-8-10 12:25:30",
//                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0);
//        parkingOrderRepository.save(parkingOrder);
////        then
//        mockMvc.perform(patch("/parkingOrders/" + orderId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.binary").value(returnBinary));
//    }
}
