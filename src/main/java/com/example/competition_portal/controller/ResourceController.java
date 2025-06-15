package com.example.competition_portal.controller;

import com.example.competition_portal.entity.RefInfo;
import com.example.competition_portal.repository.RefInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8081") // 替换为你的前端地址
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private RefInfoRepository refInfoRepository;

    @GetMapping
    public List<RefInfo> getAllResources() {
        return refInfoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefInfo> getResourceById(@PathVariable Long id) {
        return refInfoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/classify/{classify}")
    public List<RefInfo> getResourcesByClassify(@PathVariable String classify) {
        return refInfoRepository.findByClassification(classify);
    }

    @GetMapping("/search")
    public List<RefInfo> searchResources(@RequestParam String keyword) {
        return refInfoRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword);
    }

    @PostMapping
    public ResponseEntity<RefInfo> createResource(@RequestBody RefInfo refInfo) {
        try {
            // 设置默认值
            if (refInfo.getClassification() == null) {
                refInfo.setClassification("其他");
            }

            // 确保适用竞赛字段不为空
            if (refInfo.getApplyCompetition() == null || refInfo.getApplyCompetition().isEmpty()) {
                refInfo.setApplyCompetition("通用");
            }

            // 保存资源
            RefInfo savedRef = refInfoRepository.save(refInfo);
            return ResponseEntity.ok(savedRef);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}