package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByParkingLotId(Integer parkingLotId);

    Comment findByOrderId(Integer orderId);
}
