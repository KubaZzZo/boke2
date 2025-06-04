package com.neu.boke2.repository;

import com.neu.boke2.entity.PostTag;
import com.neu.boke2.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
} 