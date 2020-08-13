package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.constants.StatusConstants;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        Comment mockComment = new Comment(1, orderId, parkingLotId, userId, score, content,null,null);
        given(commentRepository.save(mockComment)).willReturn(mockComment);
        given(parkingOrderRepository.findById(orderId)).willReturn(
                Optional.of(new ParkingOrder(1, "", "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 1, "2020-8-10 14:25:30",
                        StatusConstants.WAIT_FOR_SURE, "浙A1063警", 10.0)));
        //when
        CommentResponse returnCommentResponse = commentService.addComment(mockComment);
        //then
        assertEquals(1, returnCommentResponse.getUserId());
    }

    @Test
    void should_throw_no_permission_when_add_comment_given_comment_user_id_not_equals_order_user_id() {
        //given
        Integer orderId = 1;
        Integer parkingLotId = 1;
        Integer userId = 1;
        Double score = 3.5;
        String content = "停车场非常干净";
        Comment mockComment = new Comment(1, orderId, parkingLotId, userId, score, content,null,null);
        given(commentRepository.save(mockComment)).willReturn(mockComment);
        given(parkingOrderRepository.findById(orderId)).willReturn(
                java.util.Optional.of(new ParkingOrder(1, "", "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 2, 1, "2020-8-10 14:25:30",
                        StatusConstants.WAIT_FOR_SURE, "浙A1063警", 10.0)));
        //when
        Exception exception = assertThrows(NoAuthorityException.class, () -> commentService.addComment(mockComment));

        //then
        assertEquals("没有权限", exception.getMessage());
    }

    @Test
    void should_throw_orderNotExistException_when_add_comment_given_comment_order_id_not_exist() {
        //given
        Integer orderId = 1;
        Integer parkingLotId = 1;
        Integer userId = 1;
        Double score = 3.5;
        String content = "停车场非常干净";
        Comment mockComment = new Comment(1, orderId, parkingLotId, userId, score, content,null,null);
        given(commentRepository.save(mockComment)).willReturn(mockComment);
        given(parkingOrderRepository.findById(orderId)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(OrderNotExistException.class, () -> commentService.addComment(mockComment));

        //then
        assertEquals("订单不存在，无法确认", exception.getMessage());
    }

    @Test
    void should_throw_no_permission_when_add_comment_given_comment_parkinglot_id_not_equals_order_parkinglot_id() {
        //given
        Integer orderId = 1;
        Integer parkingLotId = 1;
        Integer userId = 1;
        Double score = 3.5;
        String content = "停车场非常干净";
        Comment mockComment = new Comment(1, orderId, parkingLotId, userId, score, content,null,null);
        given(commentRepository.save(mockComment)).willReturn(mockComment);
        given(parkingOrderRepository.findById(orderId)).willReturn(
                java.util.Optional.of(new ParkingOrder(1, "", "2020-8-10 12:25:30",
                        "2020-8-10 14:25:30", 1, 2, "2020-8-10 14:25:30",
                        StatusConstants.WAIT_FOR_SURE, "浙A1063警", 10.0)));
        //when
        Exception exception = assertThrows(NoAuthorityException.class, () -> commentService.addComment(mockComment));

        //then
        assertEquals("没有权限", exception.getMessage());
    }

    @Test
    void should_return_all_comments_when_get_comments_given_parking_lot_id() {
        //given
        Integer parkingLotId = 1;
        List<Comment> commentList = Arrays.asList(
                new Comment(1, 1, parkingLotId, 1, 5.0, "111",null,null),
                new Comment(2, 2, parkingLotId, 1, 4.0, "222",null,null)
        );

        commentRepository.saveAll(commentList);
        given(commentRepository.findAllByParkingLotId(parkingLotId)).willReturn(commentList);
        //when
        CommentResponse returnCommentResponse = commentService.getAllComment(parkingLotId);

        //then
        assertEquals(4.5,returnCommentResponse.getAvgScore());
        assertEquals(commentList,returnCommentResponse.getComments());
    }

    @Test
    void should_return_comment_when_get_comment_given_order__id() {
        //given
        Integer orderId = 1;
        Comment comment = new Comment(1, orderId, 1, 1, 5.0, "111",null,null);

        commentRepository.save(comment);
        given(commentRepository.findByOrderId(orderId)).willReturn(comment);
        //when
        CommentResponse commentResponse = commentService.getComment(orderId);

        //then
        assertEquals(CommentMapper.convertToCommentResponse(comment),commentResponse);
    }

    @Test
    void should_throw_no_comment_exception_when_get_comment_given_order_id_with_no_comment() {
        //given
        Integer orderId = 1;
        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> commentService.getComment(orderId));

        //then
        assertEquals("该订单没有评论", exception.getMessage());
    }
}
