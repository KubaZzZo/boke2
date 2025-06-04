package com.neu.boke2.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "post_categories")
@IdClass(PostCategoryId.class)
public class PostCategory {
    @Id
    private Long postId;

    @Id
    private Long categoryId;
} 