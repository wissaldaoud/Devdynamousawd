package com.pi.forum.controller;

import com.pi.forum.entities.Comment;
import com.pi.forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forums")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comment/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(
            @PathVariable int postId) {
        List<Comment> comments = commentService.getCommentsByPost(postId);

        return ResponseEntity.ok(comments);
    }
    @PostMapping("/comment/post/{postId}/{userId}")
    public ResponseEntity<Comment> createComment(@PathVariable int postId,@PathVariable int userId, @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.createComment(postId,userId, comment));
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    // Update a Comment
    @PutMapping("/comment/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable int id, @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.updateComment(id, comment));
    }

    // Delete a Comment
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment deleted successfully!");
    }
}
