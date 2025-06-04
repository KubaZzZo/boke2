package com.neu.boke2.service;

import com.neu.boke2.entity.Post;
import com.neu.boke2.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public Page<Post> findPage(String title, String status, String category, Pageable pageable) {
        // 这里只做标题和状态的简单筛选，category可根据实际需求扩展
        Post probe = new Post();
        if (title != null && !title.isEmpty()) probe.setTitle(title);
        if (status != null && !status.isEmpty()) probe.setStatus(Post.Status.valueOf(status));
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<Post> example = Example.of(probe, matcher);
        return postRepository.findAll(example, pageable);
    }
} 