package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.model.Comment;
import com.oocl.parkingreservationservice.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class commentServiceTest {
    private CommentService commentService;
    private CommentRepository commentRepository;

    @BeforeEach
    public void init() {
        commentRepository = Mockito.mock(CommentRepository.class);
        commentService = new CommentService(commentRepository);
    }

    @Test
    void should_return_comment_when_add_comment_given_comment() {
        //given
        Integer orderId = 1;
        Integer parkingLotId = 1;
        Integer userId = 1;
        Double score = 3.5;
        String content = "停车场非常干净";
        Comment mockComment = new Comment(1,orderId,parkingLotId,userId,score,content);
        given(commentRepository.save(mockComment)).willReturn(mockComment);
        //when
        CommentResponse returnCommentResponse = commentService.addComment(orderId,parkingLotId,userId,score,content);
        //then
        assertEquals(1, returnCommentResponse.getUserId());
    }
}
