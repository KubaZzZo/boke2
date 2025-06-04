package com.neu.boke2.controller;

import com.neu.boke2.common.ApiResponse;
import com.neu.boke2.entity.Tag;
import com.neu.boke2.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public ApiResponse<List<Tag>> getAllTags() {
        return ApiResponse.success(tagService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Tag> getTagById(@PathVariable Long id) {
        return tagService.findById(id)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.error("标签不存在"));
    }

    @PostMapping
    public ApiResponse<Tag> createTag(@RequestBody Tag tag) {
        return ApiResponse.success(tagService.save(tag));
    }

    @PutMapping("/{id}")
    public ApiResponse<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        return ApiResponse.success(tagService.save(tag));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteById(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/page")
    public ApiResponse<Page<Tag>> getPagedTags(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Tag> tags = tagService.findByNameContaining(name, pageRequest);
        return ApiResponse.success(tags);
    }
}