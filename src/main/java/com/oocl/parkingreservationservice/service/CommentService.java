package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.repository.CommentRepository;

public class CommentService {
    private CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponse addComment(Integer orderId, Integer parkingLotId, Integer userId, Double score, String content) {
        return null;
    }
}
