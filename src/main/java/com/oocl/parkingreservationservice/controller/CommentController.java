package com.oocl.parkingreservationservice.controller;

import com.oocl.parkingreservationservice.dto.CommentRequest;
import com.oocl.parkingreservationservice.dto.CommentResponse;
import com.oocl.parkingreservationservice.exception.NoAuthorityException;
import com.oocl.parkingreservationservice.exception.OrderNotExistException;
import com.oocl.parkingreservationservice.mapper.CommentMapper;
import com.oocl.parkingreservationservice.model.Comment;
import com.oocl.parkingreservationservice.service.CommentService;
import com.oocl.parkingreservationservice.utils.QRCodeUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(@RequestBody CommentRequest commentRequest) throws OrderNotExistException, NoAuthorityException {
        Comment comment = CommentMapper.convertToComment(commentRequest);
        return commentService.addComment(comment);
    }
//
//    public static void main(String args[]){
//        String binary = QRCodeUtil.creatRrCode("http://www.baidu.com",200,200);
//        System.out.println(binary);
//    }
}
