package com.neu.boke2.repository;

import com.neu.boke2.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByContentContaining(String content, Pageable pageable);
    
    Page<Comment> findByStatus(Comment.Status status, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE " +
           "LOWER(c.content) LIKE LOWER(:term) OR " +
           "LOWER(c.authorName) LIKE LOWER(:term) OR " +
           "LOWER(c.user.username) LIKE LOWER(:term)")
    Page<Comment> searchComments(@Param("term") String term, Pageable pageable);
}