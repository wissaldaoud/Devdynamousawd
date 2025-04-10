package com.pi.forum.service;

import com.pi.forum.entities.Like;
import com.pi.forum.entities.Post;
import com.pi.forum.repository.LikeRepository;
import com.pi.forum.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public void toggleLike(int postId, int userId) {
        // Check if post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        // Check if like already exists
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);

        if (existingLike.isPresent()) {
            // Unlike - remove the existing like
            likeRepository.delete(existingLike.get());
        } else {
            // Like - create new like
            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setUserId(userId);
            likeRepository.save(newLike);
        }
    }

    public boolean hasUserLikedPost(int postId, int userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    public int getPostLikeCount(int postId) {
        return likeRepository.countByPostId(postId);
    }
}
