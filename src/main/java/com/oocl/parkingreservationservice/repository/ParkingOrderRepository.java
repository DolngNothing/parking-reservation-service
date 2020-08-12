package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, Integer> {

    List<ParkingOrder> findAllByUserId(Integer id);

}
