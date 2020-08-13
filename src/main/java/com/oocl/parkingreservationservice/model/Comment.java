package com.oocl.parkingreservationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderId;
    private Integer parkingLotId;
    private Integer userId;
    private Double score;
    private String content;
    @Column(name = "username")
    private String userName;
    private String createTime = DateFormat.getDateTimeInstance().format(new Date());
}
