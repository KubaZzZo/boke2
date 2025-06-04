package com.neu.boke2.entity;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "author_name", length = 100)
    private String authorName;

    @Column(name = "author_email", length = 100)
    private String authorEmail;

    @Column(name = "author_url", length = 255)
    private String authorUrl;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status = Status.PENDING;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public enum Status {
        APPROVED("已通过"),
        PENDING("待审核"),
        SPAM("垃圾评论"),
        REJECTED("已拒绝");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public static Status fromString(String value) {
            if (value == null) return PENDING;
            try {
                return Status.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return PENDING;
            }
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = Status.fromString(status);
    }

    public String getStatusDescription() {
        return status != null ? status.getDescription() : Status.PENDING.getDescription();
    }
}