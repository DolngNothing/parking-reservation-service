package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
