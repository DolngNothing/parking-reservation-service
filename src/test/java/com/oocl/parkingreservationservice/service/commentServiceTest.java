package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.StatusContants;
import com.oocl.parkingreservationservice.dto.CommentRequest;
import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.exception.NoAuthorityException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;
import com.oocl.parkingreservationservice.mapper.CommentMapper;
import com.oocl.parkingreservationservice.model.Comment;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.CommentRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

public class commentServiceTest {
    private CommentService commentService;
    private CommentRepository commentRepository;
    private ParkingOrderRepository parkingOrderRepository;

    @BeforeEach
    public void init() {
        commentRepository = Mockito.mock(CommentRepository.class);
        parkingOrderRepository = Mockito.mock(ParkingOrderRepository.class);
        commentService = new CommentService(commentRepository,parkingOrderRepository);
    }

    @Test
    void should_return_comment_when_add_comment_given_comment() throws OrderNotExistException, NoAuthorityException {
        //given
        Integer orderId = 1;
        Integer parkingLotId = 1;
        Integer userId = 1;
        Double score = 3.5;
        String content = "停车场非常干净";
        Comment mockComment = new Comment(1, orderId, parkingLotId, userId, score, content);
        given(commentRepository.save(mockComment)).willReturn(mockComment);
        //when
        CommentResponse returnCommentResponse = commentService.addComment(mockComment);
        //then
        assertEquals(1, returnCommentResponse.getUserId());
    }

    @Test
    void should_throw_no_permission_when_add_comment_given_comment_userId_not_equals_order_userId() {
        //given
        Integer orderId = 1;
        Integer parkingLotId = 1;
        Integer userId = 1;
        Double score = 3.5;
        String content = "停车场非常干净";
        Comment mockComment = new Comment(1, orderId, parkingLotId, userId, score, content);
        given(commentRepository.save(mockComment)).willReturn(mockComment);
        given(parkingOrderRepository.findById(orderId)).willReturn(
                java.util.Optional.of(new ParkingOrder(1, 1L, "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 2, 1, "2020-8-10 14:25:30",
                        StatusContants.WAIT_FOR_SURE, "浙A1063警", 10.0)));
        //when
        Exception exception = assertThrows(NoAuthorityException.class, () -> commentService.addComment(mockComment));

        //then
        assertEquals("没有权限", exception.getMessage());
    }
}
