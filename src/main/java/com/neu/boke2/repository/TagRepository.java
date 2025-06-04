package com.neu.boke2.repository;

import com.neu.boke2.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findByNameContaining(String name, Pageable pageable);
    Optional<Tag> findBySlug(String slug);
}