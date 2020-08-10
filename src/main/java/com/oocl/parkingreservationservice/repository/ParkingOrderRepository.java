package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, Integer> {
//    @Modifying
//    @Transactional
//    @Query(value = "update parking_order set status = ?1 where id = ?2",nativeQuery = true)
//    void updateStatus(String status, int orderId);
}
