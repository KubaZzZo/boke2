package com.neu.boke2.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class PostCategoryId implements Serializable {
    private Long postId;
    private Long categoryId;
} 