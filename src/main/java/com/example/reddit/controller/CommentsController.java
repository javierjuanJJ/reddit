package com.example.reddit.controller;

import com.example.reddit.dto.CommentsDto;
import com.example.reddit.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.createComment(commentsDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable("id") Long id) {
        System.out.println("dentro" + id);
        return status(HttpStatus.OK)
                .body(commentService.getCommentByPost(id));
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@PathVariable("userName") String userName) {
        return status(HttpStatus.OK).body(commentService.getCommentsByUser(userName));
    }
}
