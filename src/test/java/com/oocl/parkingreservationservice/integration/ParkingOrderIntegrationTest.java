package com.oocl.parkingreservationservice.integration;

import com.oocl.parkingreservationservice.constants.StatusContants;
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

import java.util.Date;

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
    MockMvc mockMvc;
    private int OrderId;
    private int parkLotId;

    @BeforeEach
    void init() {
        ParkingOrder firstOrder = new ParkingOrder(1, 1L, "2020-08-10 12:25:30",
                "2020-08-10 14:25:30", 1, 1, "2020-08-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);
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
                "}";
        //when
        mockMvc.perform(post(("/parkingOrders")).contentType(MediaType.APPLICATION_JSON).content(orderInfo))
                .andExpect(status().isBadRequest());
        //then
    }

    @Test
    void should_return_employee_when_hit_get_employee_endpoint_given_employee_id() throws Exception {
        //given
        ParkingOrder order = new ParkingOrder(1, 1L, "2020-08-10 12:25:30",
                "2020-08-10 14:25:30", 1, 1, "2020-08-10 14:25:30", StatusContants.WAIT_FOR_SURE, "1234", 10.0);
        ParkingOrder parkingOrder = parkingOrderRepository.save(order);

        //when
        mockMvc.perform(get("/parkingOrders/" + parkingOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(parkingOrder.getId()));
    }
}
