package com.oocl.parkingreservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentRequest {
    private Integer id;
    private Integer orderId;
    private Integer parkingLotId;
    private Integer userId;
    private Double score;
    private String content;
    private String userName;
    private String createTime;
}
