package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, Integer> {
}
