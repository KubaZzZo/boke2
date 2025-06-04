package com.neu.boke2.repository;

import com.neu.boke2.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
} 