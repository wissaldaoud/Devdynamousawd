package com.pi.forum.repository;

import com.pi.forum.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p ORDER BY p.created_at DESC")
    List<Post> findPostsLatest();


    Page<Post> findByTagIgnoreCase(String tag, Pageable pageable);
    // List<Post> findByTagIgnoreCase(String tag);


    @Query("SELECT DISTINCT LOWER(p.tag) FROM Post p")
    Set<String> findDistinctTags();

}