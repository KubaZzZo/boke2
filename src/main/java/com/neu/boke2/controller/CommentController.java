package com.neu.boke2.controller;

import com.neu.boke2.common.ApiResponse;
import com.neu.boke2.entity.Comment;
import com.neu.boke2.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @GetMapping("/page")
    public ApiResponse<Page<Comment>> getPagedComments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {
        try {
            logger.info("分页查询评论: page={}, size={}, searchTerm={}", page, size, searchTerm);
            PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Comment> comments = commentService.search(searchTerm, pageRequest);
            logger.info("查询到{}条评论记录", comments.getTotalElements());
            return ApiResponse.success(comments);
        } catch (Exception e) {
            logger.error("分页查询评论失败", e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Comment>> getAllComments() {
        try {
            logger.info("查询所有评论");
            List<Comment> comments = commentService.findAll();
            logger.info("查询到{}条评论记录", comments.size());
            return ApiResponse.success(comments);
        } catch (Exception e) {
            logger.error("查询所有评论失败", e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Comment> getCommentById(@PathVariable Long id) {
        try {
            logger.info("根据ID查询评论: id={}", id);
            return commentService.findById(id)
                    .map(comment -> {
                        logger.info("找到评论: {}", comment);
                        return ApiResponse.success(comment);
                    })
                    .orElseGet(() -> {
                        logger.warn("未找到评论: id={}", id);
                        return ApiResponse.error("评论不存在");
                    });
        } catch (Exception e) {
            logger.error("查询评论失败: id=" + id, e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<Comment> createComment(@RequestBody Comment comment) {
        try {
            logger.info("创建评论: {}", comment);
            if (comment.getStatus() == null) {
                comment.setStatus(Comment.Status.PENDING);
            }
            Comment saved = commentService.save(comment);
            logger.info("评论创建成功: {}", saved);
            return ApiResponse.success(saved);
        } catch (Exception e) {
            logger.error("创建评论失败", e);
            return ApiResponse.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        try {
            logger.info("更新评论: id={}, comment={}", id, comment);
            return commentService.findById(id)
                    .map(existing -> {
                        comment.setId(id);
                        // 保留创建时间
                        if (existing.getCreatedAt() != null) {
                            comment.setCreatedAt(existing.getCreatedAt());
                        }
                        Comment updated = commentService.save(comment);
                        logger.info("评论更新成功: {}", updated);
                        return ApiResponse.success(updated);
                    })
                    .orElseGet(() -> {
                        logger.warn("要更新的评论不存在: id={}", id);
                        return ApiResponse.error("评论不存在");
                    });
        } catch (Exception e) {
            logger.error("更新评论失败: id=" + id, e);
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Comment> updateCommentStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        try {
            logger.info("更新评论状态: id={}, status={}", id, statusUpdate.get("status"));
            String newStatus = statusUpdate.get("status");
            if (newStatus == null) {
                return ApiResponse.error("状态不能为空");
            }

            Comment updated = commentService.updateStatus(id, newStatus);
            logger.info("评论状态更新成功: {}", updated);
            return ApiResponse.success(updated);
        } catch (Exception e) {
            logger.error("更新评论状态失败: id=" + id, e);
            return ApiResponse.error("更新状态失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteComment(@PathVariable Long id) {
        try {
            logger.info("删除评论: id={}", id);
            if (commentService.findById(id).isPresent()) {
                commentService.deleteById(id);
                logger.info("评论删除成功: id={}", id);
                return ApiResponse.success(null);
            } else {
                logger.warn("要删除的评论不存在: id={}", id);
                return ApiResponse.error("评论不存在");
            }
        } catch (Exception e) {
            logger.error("删除评论失败: id=" + id, e);
            return ApiResponse.error("删除失败: " + e.getMessage());
        }
    }
}