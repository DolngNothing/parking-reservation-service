package com.oocl.parkingreservationservice.repository;

import com.oocl.parkingreservationservice.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author XUAL7
 */
public interface ParkingLotRepository extends JpaRepository<ParkingLot,Integer> {
    double findPriceById(Integer id);
}
