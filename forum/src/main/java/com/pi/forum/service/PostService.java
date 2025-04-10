package com.pi.forum.service;

import com.pi.forum.entities.Post;
import com.pi.forum.entities.PostType;
import com.pi.forum.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private MailService mailService;

    public Post createPost(Post post) {
        this.mailService.sendEmail("slimfady.hanafi7@gmail.com","Ajout d'un Post - Notification","Nous souhaitons vous informer qu'un post a été ajouté");
        return postRepository.save(post);
    }

    public Post getPostById(int id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post updatePost(int id, Post updatedPost) {
        Post post = getPostById(id);
        post.setContent(updatedPost.getContent());
        return postRepository.save(post);
    }

    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public List<Post> getRecentPosts() {
        return postRepository.findPostsLatest();
    }
    public Page<Post> getPostsByTag(String tag, Pageable pageable) {
        return postRepository.findByTagIgnoreCase(tag, pageable);
    }
  /*  public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTagIgnoreCase(tag);
    }*/

    public Set<String> getListOfTags() {
        Set<String> rawTags = postRepository.findDistinctTags();
        Set<String> normalizedTags = new HashSet<>();
        for (String tag : rawTags) {
            if (tag != null && !tag.trim().isEmpty()) {
                normalizedTags.add(tag.trim().toLowerCase());
            }
        }
        return normalizedTags;
    }

    public Map<String, Object> getPostStatistics() {
        // Initialize counters for each post type
        int solvingProblemPosts = 0;
        int freelancerPosts = 0;
        int solvingProblemInteractions = 0;
        int freelancerInteractions = 0;

        // Fetch all posts from the repository
        List<Post> posts = postRepository.findAll();

        // Loop through posts to calculate count and interactions
        for (Post post : posts) {
            if (post.getPostType() == PostType.SOLVING_PROBLEM) {
                solvingProblemPosts++;
                solvingProblemInteractions += post.getComments().size() + post.getLikes().size();
            } else if (post.getPostType() == PostType.FREELANCER) {
                freelancerPosts++;
                freelancerInteractions += post.getComments().size() + post.getLikes().size();
            }
        }

        // Create response maps for posts and interactions
        Map<String, Object> postCounts = new HashMap<>();
        postCounts.put("count", Arrays.asList(solvingProblemPosts, freelancerPosts));
        postCounts.put("labels", Arrays.asList("SOLVING_PROBLEM", "FREELANCER"));

        Map<String, Object> postInteractions = new HashMap<>();
        postInteractions.put("count", Arrays.asList(solvingProblemInteractions, freelancerInteractions));
        postInteractions.put("labels", Arrays.asList("SOLVING_PROBLEM", "FREELANCER"));

        // Combine both statistics into a final response map
        Map<String, Object> response = new HashMap<>();
        response.put("postCounts", postCounts);
        response.put("postInteractions", postInteractions);

        return response;
    }
}