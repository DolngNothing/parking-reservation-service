package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findFirstByEmail(String email);

    @Query(value = "select * from user where phone_number=?1", nativeQuery = true)
    User findByPhone(String phone);
}
