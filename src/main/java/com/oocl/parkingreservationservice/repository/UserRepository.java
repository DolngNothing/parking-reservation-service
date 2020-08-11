package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Integer> {
   User findFirstByEmail(String email);
}
