package com.neu.boke2.repository;

import com.neu.boke2.entity.PostCategory;
import com.neu.boke2.entity.PostCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, PostCategoryId> {
} 