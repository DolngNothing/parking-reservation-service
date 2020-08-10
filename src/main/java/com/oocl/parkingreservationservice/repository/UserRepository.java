package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository  extends JpaRepository<User, Integer> {
    List<User> findFirst1ByEmail(String email);
}
