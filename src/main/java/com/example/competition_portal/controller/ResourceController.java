package com.example.competition_portal.controller;

import com.example.competition_portal.entity.RefInfo;
import com.example.competition_portal.repository.RefInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}