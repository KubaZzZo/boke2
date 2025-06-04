package com.neu.boke2.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "post_tags")
@IdClass(PostTagId.class)
public class PostTag {
    @Id
    private Long postId;

    @Id
    private Long tagId;
} 