package com.neu.boke2.controller;

import com.neu.boke2.common.ApiResponse;
import com.neu.boke2.entity.Category;
import com.neu.boke2.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public ApiResponse<Page<Category>> getPagedCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name) {
        try {
            logger.info("分页查询分类: page={}, size={}, name={}", page, size, name);
            
            PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<Category> categories = categoryService.findByNameContaining(name, pageRequest);
            
            logger.info("查询到{}条分类记录", categories.getTotalElements());
            return ApiResponse.success(categories);
        } catch (Exception e) {
            logger.error("分页查询分类失败", e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Category>> getAllCategories() {
        try {
            logger.info("查询所有分类");
            List<Category> categories = categoryService.findAll();
            logger.info("查询到{}条分类记录", categories.size());
            return ApiResponse.success(categories);
        } catch (Exception e) {
            logger.error("查询所有分类失败", e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Category> getCategoryById(@PathVariable Long id) {
        try {
            logger.info("根据ID查询分类: id={}", id);
            return categoryService.findById(id)
                    .map(category -> {
                        logger.info("找到分类: {}", category);
                        return ApiResponse.success(category);
                    })
                    .orElseGet(() -> {
                        logger.warn("未找到分类: id={}", id);
                        return ApiResponse.error("分类不存在");
                    });
        } catch (Exception e) {
            logger.error("查询分类失败: id=" + id, e);
            return ApiResponse.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<Category> createCategory(@RequestBody Category category) {
        try {
            logger.info("创建分类: {}", category);
            Category saved = categoryService.save(category);
            logger.info("分类创建成功: {}", saved);
            return ApiResponse.success(saved);
        } catch (Exception e) {
            logger.error("创建分类失败", e);
            return ApiResponse.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            logger.info("更新分类: id={}, category={}", id, category);
            category.setId(id);
            Category updated = categoryService.save(category);
            logger.info("分类更新成功: {}", updated);
            return ApiResponse.success(updated);
        } catch (Exception e) {
            logger.error("更新分类失败: id=" + id, e);
            return ApiResponse.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        try {
            logger.info("删除分类: id={}", id);
            categoryService.deleteById(id);
            logger.info("分类删除成功: id={}", id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            logger.error("删除分类失败: id=" + id, e);
            return ApiResponse.error("删除失败: " + e.getMessage());
        }
    }
}