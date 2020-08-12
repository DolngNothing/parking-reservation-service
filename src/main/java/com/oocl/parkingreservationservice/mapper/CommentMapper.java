package com.oocl.parkingreservationservice.mapper;

import com.oocl.parkingreservationservice.dto.CommentRequest;
import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.model.Comment;
import org.springframework.beans.BeanUtils;

public class CommentMapper {
    public static CommentResponse convertToCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        BeanUtils.copyProperties(comment, commentResponse);
        return commentResponse;
    }

    public static Comment convertToComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentRequest, comment);
        return comment;
    }
}
