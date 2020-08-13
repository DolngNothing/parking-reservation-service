package com.oocl.parkingreservationservice.integration;

import com.oocl.parkingreservationservice.constants.StatusConstants;
import com.oocl.parkingreservationservice.controller.CommentController;
import com.oocl.parkingreservationservice.handler.GlobalExceptionHandler;
import com.oocl.parkingreservationservice.model.Comment;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.CommentRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentIntegrationTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentController commentController;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private ParkingOrderRepository parkingOrderRepository;
    private ParkingOrder parkingOrder;
    MockMvc mockMvc;
    private int parkingLotId;

    @BeforeEach
    void init() {
        parkingOrder = parkingOrderRepository.save(new ParkingOrder(1, null, "2020-8-10 12:25:30",
                "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30", StatusConstants.WAIT_FOR_SURE, "1234", 10.0));
        mockMvc = MockMvcBuilders.standaloneSetup(commentController, globalExceptionHandler).build();
    }

    @AfterEach
    void tearDown() {
        parkingOrderRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void should_return_comment_response_when_hit_add_comment_endpoint_given_info() throws Exception {
        //given
        String commentInfo = "{\n" +
                "    \"id\": 1,\n" +
                "    \"orderId\":" + parkingOrder.getId() + ",\n" +
                "    \"userId\": " + parkingOrder.getUserId() + ",\n" +
                "    \"parkingLotId\": " + parkingOrder.getParkingLotId() + ",\n" +
                "    \"score\":3.5,\n" +
                "    \"content\":\"好看\"\n" +
                "}";
        //when
        mockMvc.perform(post(("/comments")).contentType(MediaType.APPLICATION_JSON).content(commentInfo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.orderId").isNumber())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.parkingLotId").isNumber())
                .andExpect(jsonPath("$.score").value(3.5))
                .andExpect(jsonPath("$.content").value("好看"));
    }

    @Test
    void should_return_avg_score_and_comments_when_hit_get_comments_endpoint_given_parkinglot_id() throws Exception {
        //given
        parkingLotId = 100;
        List<Comment> commentList = Arrays.asList(
                new Comment(null, 1, parkingLotId, 1, 5.0, "111",null,null),
                new Comment(null, 2, parkingLotId, 1, 4.0, "222",null,null)
        );
        commentRepository.saveAll(commentList);

        //when
        mockMvc.perform(get("/comments").param("parkingLotId", String.valueOf(parkingLotId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.avgScore").value(4.5));
    }

    @Test
    void should_return_comment_when_hit_get_comment_endpoint_given_order_id() throws Exception {
        //given
        Comment comment = new Comment(null, parkingOrder.getId(), 1, 1, 5.0, "111",null,null);
        Comment comment2 = commentRepository.save(comment);
        //whengit
        mockMvc.perform(get("/comments").param("orderId", String.valueOf(comment2.getOrderId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("111"));
    }

}
