package com.neu.boke2.controller;

import com.neu.boke2.common.ApiResponse;
import com.neu.boke2.entity.Post;
import com.neu.boke2.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ApiResponse<List<Post>> getAllPosts() {
        return ApiResponse.success(postService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Post> getPostById(@PathVariable Long id) {
        return postService.findById(id)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.error("文章不存在"));
    }

    @PostMapping
    public ApiResponse<Post> createPost(@RequestBody Post post) {
        return ApiResponse.success(postService.save(post));
    }

    @PutMapping("/{id}")
    public ApiResponse<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);
        return ApiResponse.success(postService.save(post));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id) {
        postService.deleteById(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/batch-delete")
    public ApiResponse<Void> batchDelete(@RequestBody List<Long> ids) {
        ids.forEach(postService::deleteById);
        return ApiResponse.success(null);
    }

    @GetMapping("/page")
    public ApiResponse<Page<Post>> getPostPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ApiResponse.success(postService.findPage(title, status, category, pageable));
    }
} 