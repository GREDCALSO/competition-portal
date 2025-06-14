package com.example.competition_portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tb_FavorRecord")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavorRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "folderID")
    private FavorFolder folder;

    @ManyToOne
    @JoinColumn(name = "refID", nullable = false)
    private RefInfo ref;

    @Temporal(TemporalType.TIMESTAMP)
    private Date collectTime = new Date();
}