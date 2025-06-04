package com.neu.boke2.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_name", length = 100)
    private String siteName;

    @Column(name = "site_desc", length = 255)
    private String siteDesc;

    @Column(name = "site_logo", length = 255)
    private String siteLogo;

    @Column(name = "icp", length = 50)
    private String icp;
} 