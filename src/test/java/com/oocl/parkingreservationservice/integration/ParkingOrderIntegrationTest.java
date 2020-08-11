package com.oocl.parkingreservationservice.integration;

import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ParkingOrderIntegrationTest {

    @Autowired
    ParkingOrderRepository parkingOrderRepository;
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
        String orderInfo = "{\n" +
                "    \"email\":\"1214852999@qq.com\",\n" +
                "    \"parkingStartTime\":\"2020-08-16 00:00:00\",\n" +
                "    \"parkingEndTime\":\"2020-08-17 00:00:00\",\n" +
                "    \"parkingLotId\":1,\n" +
                "    \"carNumber\":\"浙A1063警\",\n" +
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
        String orderInfo = "{\n" +
                "    \"email\":\"1214852999@q.com\",\n" +
                "    \"parkingStartTime\":\"2020-08-08 00:00:00\",\n" +
                "    \"parkingEndTime\":\"2020-08-07 00:00:00\",\n" +
                "    \"parkingLotId\":1,\n" +
                "    \"carNumber\":\"浙A1063\",\n" +
                "}";
        //when
        mockMvc.perform(post(("/parkingOrders")).contentType(MediaType.APPLICATION_JSON).content(orderInfo))
                .andExpect(status().isBadRequest());
        //then
    }
}
