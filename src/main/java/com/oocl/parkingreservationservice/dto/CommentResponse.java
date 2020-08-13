package com.oocl.parkingreservationservice.dto;

import com.oocl.parkingreservationservice.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponse {
    private Integer id;
    private Integer orderId;
    private Integer parkingLotId;
    private Integer userId;
    private Double score;
    private String content;
    private String userName;
    private String createTime;
    private List<Comment> comments;
    private Double avgScore;
}
