package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.dto.CommentRequest;
import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.mapper.CommentMapper;
import com.oocl.parkingreservationservice.model.Comment;
import com.oocl.parkingreservationservice.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponse addComment(Comment comment) {
        Comment returnComment = commentRepository.save(comment);
        CommentResponse commentResponse = CommentMapper.convertToCommentResponse(returnComment);
        return commentResponse;
    }
}
