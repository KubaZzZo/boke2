package com.neu.boke2.entity;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        updatedAt = createdAt;
        if (slug == null || slug.isEmpty()) {
            slug = name.toLowerCase().replaceAll("\\s+", "-")
                      .replaceAll("[^a-z0-9\\-]", "");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
        if (slug == null || slug.isEmpty()) {
            slug = name.toLowerCase().replaceAll("\\s+", "-")
                      .replaceAll("[^a-z0-9\\-]", "");
        }
    }
} 