package com.example.competition_portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_FavorFolder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavorFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private UserInfo user;

    private String folderName;
    private String folderDesc;
}