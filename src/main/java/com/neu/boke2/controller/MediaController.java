package com.neu.boke2.controller;

import com.neu.boke2.common.ApiResponse;
import com.neu.boke2.entity.Media;
import com.neu.boke2.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/media")
public class MediaController {
    @Autowired
    private MediaService mediaService;

    @GetMapping
    public ApiResponse<List<Media>> getAllMedia() {
        return ApiResponse.success(mediaService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Media> getMediaById(@PathVariable Long id) {
        return mediaService.findById(id)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.error("媒体文件不存在"));
    }

    @PostMapping
    public ApiResponse<Media> createMedia(@RequestBody Media media) {
        return ApiResponse.success(mediaService.save(media));
    }

    @PutMapping("/{id}")
    public ApiResponse<Media> updateMedia(@PathVariable Long id, @RequestBody Media media) {
        media.setId(id);
        return ApiResponse.success(mediaService.save(media));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteById(id);
        return ApiResponse.success(null);
    }
} 