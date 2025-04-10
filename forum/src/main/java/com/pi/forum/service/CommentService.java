package com.pi.forum.service;

import com.pi.forum.entities.Comment;
import com.pi.forum.entities.Post;
import com.pi.forum.repository.CommentRepository;
import com.pi.forum.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public List<Comment> getCommentsByPost(int postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment createComment(int postId, int userId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setPost(post);
        comment.setUserId(userId);
        return commentRepository.save(comment);
    }

    public Comment getCommentById(int id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    // Update a Comment
    public Comment updateComment(int id, Comment updatedComment) {
        Comment comment = getCommentById(id);
        comment.setContent(updatedComment.getContent());
        return commentRepository.save(comment);
    }

    // Delete a Comment
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }

}
