package com.example.competition_portal.repository;

import com.example.competition_portal.entity.FavorRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavorRecordRepository extends JpaRepository<FavorRecord, Long> {
    List<FavorRecord> findByUser_UserID(Long userId);
    Optional<FavorRecord> findByUser_UserIDAndRef_RefID(Long userId, Long refId);
    boolean existsByUser_UserIDAndRef_RefID(Long userId, Long refId);
}