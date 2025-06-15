package com.example.competition_portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_RefInfo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refID;

    private String title;
    private String description;

    private String classification; // 视频、博客、文档、开源项目
    private String label;    // 学科标签

    @Column(name = "apply_competition")
    private String applyCompetition;

    private String url;
    @Transient
    private String filePath; // 文档存储路径

    public RefInfo(String title, String description, String classification,
                   String label, String applyCompetition, String url) {
        this.title = title;
        this.description = description;
        this.classification = classification;
        this.label = label;
        this.applyCompetition = applyCompetition;
        this.url = url;
    }
}