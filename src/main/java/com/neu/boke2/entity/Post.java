package com.neu.boke2.entity;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String excerpt;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status = Status.draft;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Visibility visibility = Visibility.public_;

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_status", length = 20)
    private CommentStatus commentStatus = CommentStatus.open;

    @Column(name = "featured_image_url", length = 255)
    private String featuredImageUrl;

    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "published_at")
    private Timestamp publishedAt;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public enum Status {
        draft, published, pending, trashed
    }
    public enum Visibility {
        public_, private_
    }
    public enum CommentStatus {
        open, closed
    }
} 