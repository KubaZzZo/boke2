package com.neu.boke2.service;

import com.neu.boke2.entity.Tag;
import com.neu.boke2.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    @Transactional
    public Tag save(Tag tag) {
        if (tag.getSlug() == null || tag.getSlug().trim().isEmpty()) {
            tag.setSlug(generateSlug(tag.getName()));
        }

        // 设置时间戳
        Timestamp now = Timestamp.from(Instant.now());
        if (tag.getId() == null) {
            tag.setCreatedAt(now);
        }
        tag.setUpdatedAt(now);

        return tagRepository.save(tag);
    }

    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

    public Page<Tag> findByNameContaining(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            return tagRepository.findAll(pageable);
        }
        return tagRepository.findByNameContaining(name.trim(), pageable);
    }

    private String generateSlug(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("标签名称不能为空");
        }

        // 转换为小写，替换空格为连字符，移除特殊字符
        String slug = name.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9\\-]", "")
                .replaceAll("-+", "-")
                .replaceAll("^-", "")
                .replaceAll("-$", "");

        // 如果转换后为空，使用时间戳
        if (slug.isEmpty()) {
            slug = "tag-" + System.currentTimeMillis();
        }

        // 检查slug是否已存在，如果存在则添加数字后缀
        String baseSlug = slug;
        int counter = 1;
        while (tagRepository.findBySlug(slug).isPresent()) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }
}