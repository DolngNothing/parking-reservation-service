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
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        ParkingOrder firstOrder = new ParkingOrder(1,1,"2020-08-10 12:25:30",
                "2020-08-10 14:25:30",1,1,"2020-08-10 14:25:30", StatusContants.WAIT_FOR_SURE,"1234",10.0);
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
}
