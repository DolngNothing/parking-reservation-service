package com.oocl.parkingreservationservice.service;

import com.oocl.parkingreservationservice.dto.CommentRequest;
import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.exception.NoAuthorityException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;
import com.oocl.parkingreservationservice.mapper.CommentMapper;
import com.oocl.parkingreservationservice.model.Comment;
import com.oocl.parkingreservationservice.model.ParkingOrder;
import com.oocl.parkingreservationservice.repository.CommentRepository;
import com.oocl.parkingreservationservice.repository.ParkingOrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private ParkingOrderRepository parkingOrderRepository;

    public CommentService(CommentRepository commentRepository, ParkingOrderRepository parkingOrderRepository) {
        this.commentRepository = commentRepository;
        this.parkingOrderRepository = parkingOrderRepository;
    }

    public CommentResponse addComment(Comment comment) throws OrderNotExistException, NoAuthorityException {
        Optional<ParkingOrder> parkingOrderOptional = parkingOrderRepository.findById(comment.getId());
        if (parkingOrderOptional.get().equals(null)) {
            throw new OrderNotExistException();
        }
        if (parkingOrderOptional.get().getId().equals(comment.getOrderId())) {
            throw new NoAuthorityException();
        }
        Comment returnComment = commentRepository.save(comment);
        return CommentMapper.convertToCommentResponse(returnComment);
    }
}
