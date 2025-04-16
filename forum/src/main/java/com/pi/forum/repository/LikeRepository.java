package com.pi.forum.repository;

import com.pi.forum.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsByPostIdAndUserId(int postId, int userId);
    Optional<Like> findByPostIdAndUserId(int postId, int userId);
    int countByPostId(int postId);
}
