package com.jkb.cmi.controller;

import com.jkb.cmi.dto.request.SaveCommentRequest;
import com.jkb.cmi.dto.request.UpdateCommentRequest;
import com.jkb.cmi.dto.response.CommentResponse;
import com.jkb.cmi.dto.response.SaveCommentResponse;
import com.jkb.cmi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<SaveCommentResponse> saveComment(@RequestBody SaveCommentRequest saveCommentRequest) {
        SaveCommentResponse saveCommentResponse = commentService.saveComment(saveCommentRequest);
        return ResponseEntity.ok(saveCommentResponse);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComment(String market) {
        List<CommentResponse> commentResponses = commentService.getComment(market);
        return ResponseEntity.ok(commentResponses);
    }

    @PutMapping
    public ResponseEntity<Void> updateComment(@RequestBody UpdateCommentRequest updateCommentRequest) {
        commentService.updateComment(updateCommentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteComment(Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}
