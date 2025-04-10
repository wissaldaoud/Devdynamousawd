package com.pi.forum.controller;

import com.pi.forum.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/forums")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/post/toggle")
    public ResponseEntity<String> toggleLike(
            @RequestParam int postId,
            @RequestParam int userId) {
        likeService.toggleLike(postId, userId);
        boolean isLiked = likeService.hasUserLikedPost(postId, userId);
        String message = isLiked ? "Post liked successfully" : "Post unliked successfully";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/post/count")
    public ResponseEntity<Integer> getLikeCount(@RequestParam int postId) {
        int likeCount = likeService.getPostLikeCount(postId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/post/status")
    public ResponseEntity<Boolean> checkLikeStatus(
            @RequestParam int postId,
            @RequestParam int userId) {
        boolean hasLiked = likeService.hasUserLikedPost(postId, userId);
        return ResponseEntity.ok(hasLiked);
    }
}
