package com.example.competition_portal.repository;

import com.example.competition_portal.entity.RefInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefInfoRepository extends JpaRepository<RefInfo, Long> {
    List<RefInfo> findByClassification(String classify);
    List<RefInfo> findByLabelContaining(String label);

    List<RefInfo> findByTitleContainingOrDescriptionContaining(String title, String title1);
}