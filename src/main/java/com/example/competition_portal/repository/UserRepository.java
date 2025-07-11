package com.example.competition_portal.repository;

import com.example.competition_portal.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUserName(String userName);

    boolean existsByUserName(String userName);
}