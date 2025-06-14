package com.example.competition_portal.repository;

import com.example.competition_portal.entity.FavorFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavorFolderRepository extends JpaRepository<FavorFolder, Long> {
    List<FavorFolder> findByUser_UserID(Long userId);
}