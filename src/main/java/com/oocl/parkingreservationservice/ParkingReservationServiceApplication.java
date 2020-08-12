package com.oocl.parkingreservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author XUAL7
 */
@SpringBootApplication
@EnableRedisHttpSession
public class ParkingReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingReservationServiceApplication.class, args);
    }

}
