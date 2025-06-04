package com.neu.boke2.service;

import com.neu.boke2.entity.Comment;
import com.neu.boke2.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    
    @Autowired
    private CommentRepository commentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Comment> search(String searchTerm, Pageable pageable) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return commentRepository.findAll(pageable);
        }
        return commentRepository.searchComments("%" + searchTerm.trim().toLowerCase() + "%", pageable);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public Comment save(Comment comment) {
        logger.debug("保存评论: {}", comment);
        try {
            Comment saved = commentRepository.save(comment);
            logger.info("评论保存成功: id={}", saved.getId());
            return saved;
        } catch (Exception e) {
            logger.error("保存评论失败", e);
            throw e;
        }
    }

    @Transactional
    public void deleteById(Long id) {
        logger.debug("删除评论: id={}", id);
        try {
            commentRepository.deleteById(id);
            logger.info("评论删除成功: id={}", id);
        } catch (Exception e) {
            logger.error("删除评论失败: id=" + id, e);
            throw e;
        }
    }

    public Page<Comment> findByStatus(Comment.Status status, Pageable pageable) {
        return commentRepository.findByStatus(status, pageable);
    }

    @Transactional
    public Comment updateStatus(Long id, String newStatus) {
        logger.debug("更新评论状态: id={}, newStatus={}", id, newStatus);
        try {
            Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
            comment.setStatus(newStatus);
            Comment updated = commentRepository.save(comment);
            logger.info("评论状态更新成功: id={}, status={}", id, updated.getStatus());
            return updated;
        } catch (Exception e) {
            logger.error("更新评论状态失败: id=" + id, e);
            throw e;
        }
    }
}