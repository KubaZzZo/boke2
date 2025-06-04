package com.neu.boke2.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint unsigned")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "bigint unsigned")
    private User user;

    @Column(name = "file_name", nullable = false, length = 255)
    @Comment("文件名")
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 512)
    @Comment("文件在服务器上的存储路径")
    private String filePath;

    @Column(name = "file_url", nullable = false, length = 512)
    @Comment("文件的访问URL")
    private String fileUrl;

    @Column(name = "file_type", nullable = false, length = 50)
    @Comment("文件MIME类型")
    private String fileType;

    @Column(name = "file_size", nullable = false)
    @Comment("文件大小(字节)")
    private Integer fileSize;

    @Column(name = "title", length = 255)
    @Comment("文件标题")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("文件描述")
    private String description;

    @Column(name = "uploaded_at", updatable = false)
    @Comment("上传时间")
    private Timestamp uploadedAt;

    @PrePersist
    protected void onCreate() {
        if (uploadedAt == null) {
            uploadedAt = new Timestamp(System.currentTimeMillis());
        }
    }
}