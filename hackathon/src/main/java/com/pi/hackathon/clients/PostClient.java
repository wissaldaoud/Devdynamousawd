package com.pi.hackathon.clients;

import com.pi.hackathon.dtos.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "forum", url = "http://localhost:8090/api/v1/forums") // LOCAL
//@FeignClient(name = "forum", url = "http://forum-service:8090/api/v1/forums")  // DOCKER
public interface PostClient {

    @GetMapping("post/{id}")
    public  Post getPostById(@PathVariable("id") int id);

    @GetMapping("posts")
    public Page<Post> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    );
}
