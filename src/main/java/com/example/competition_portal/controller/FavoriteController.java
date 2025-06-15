// FavoriteController.java
package com.example.competition_portal.controller;

import com.example.competition_portal.dto.AddFavoriteRequest;
import com.example.competition_portal.dto.FavoriteResponse;
import com.example.competition_portal.entity.FavorFolder;
import com.example.competition_portal.entity.FavorRecord;
import com.example.competition_portal.entity.RefInfo;
import com.example.competition_portal.entity.UserInfo;
import com.example.competition_portal.repository.FavorFolderRepository;
import com.example.competition_portal.repository.FavorRecordRepository;
import com.example.competition_portal.repository.RefInfoRepository;
import com.example.competition_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavorRecordRepository favorRecordRepository;

    @Autowired
    private FavorFolderRepository favorFolderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefInfoRepository refInfoRepository;

    // 添加收藏
    @PostMapping
    public ResponseEntity<FavoriteResponse> addFavorite(@RequestBody AddFavoriteRequest request) {
        FavoriteResponse response = new FavoriteResponse();

        // 验证用户存在
        Optional<UserInfo> user = userRepository.findById(request.getUserId());
        if (!user.isPresent()) {
            response.setSuccess(false);
            response.setMessage("用户不存在");
            return ResponseEntity.badRequest().body(response);
        }

        // 验证资源存在
        Optional<RefInfo> refInfo = refInfoRepository.findById(request.getRefId());
        if (!refInfo.isPresent()) {
            response.setSuccess(false);
            response.setMessage("资源不存在");
            return ResponseEntity.badRequest().body(response);
        }

        // 检查是否已收藏
        if (favorRecordRepository.existsByUser_UserIDAndRef_RefID(request.getUserId(), request.getRefId())) {
            response.setSuccess(false);
            response.setMessage("您已收藏过该资源");
            return ResponseEntity.badRequest().body(response);
        }

        // 创建收藏记录
        FavorRecord record = new FavorRecord();
        record.setUser(user.get());
        record.setRef(refInfo.get());

        // 处理收藏夹
        if (request.getFolderId() != null) {
            Optional<FavorFolder> folder = favorFolderRepository.findById(request.getFolderId());
            if (folder.isPresent() && folder.get().getUser().getUserID().equals(request.getUserId())) {
                record.setFolder(folder.get());
            }
        }

        record.setCollectTime(new Date());
        favorRecordRepository.save(record);

        response.setSuccess(true);
        response.setMessage("收藏成功");
        return ResponseEntity.ok(response);
    }

    // 取消收藏
    @DeleteMapping("/{userId}/{refId}")
    public ResponseEntity<FavoriteResponse> removeFavorite(@PathVariable Long userId, @PathVariable Long refId) {
        FavoriteResponse response = new FavoriteResponse();

        // 验证收藏记录存在
        Optional<FavorRecord> record = favorRecordRepository.findByUser_UserIDAndRef_RefID(userId, refId);
        if (!record.isPresent()) {
            response.setSuccess(false);
            response.setMessage("未找到收藏记录");
            return ResponseEntity.badRequest().body(response);
        }

        favorRecordRepository.delete(record.get());

        response.setSuccess(true);
        response.setMessage("已取消收藏");
        return ResponseEntity.ok(response);
    }

    // 获取用户收藏
    @GetMapping("/{userId}")
    public ResponseEntity<FavoriteResponse> getUserFavorites(@PathVariable Long userId) {
        FavoriteResponse response = new FavoriteResponse();

        // 验证用户存在
        if (!userRepository.existsById(userId)) {
            response.setSuccess(false);
            response.setMessage("用户不存在");
            return ResponseEntity.badRequest().body(response);
        }

        // 获取收藏的资源
        List<RefInfo> favorites = favorRecordRepository.findByUser_UserID(userId)
                .stream()
                .map(FavorRecord::getRef)
                .collect(Collectors.toList());

        // 获取收藏夹
        List<FavorFolder> folders = favorFolderRepository.findByUser_UserID(userId);

        response.setSuccess(true);
        response.setFavorites(favorites);
        response.setFolders(folders);
        return ResponseEntity.ok(response);
    }

    // 检查是否收藏
    @GetMapping("/check/{userId}/{refId}")
    public ResponseEntity<FavoriteResponse> checkFavorite(@PathVariable Long userId, @PathVariable Long refId) {
        FavoriteResponse response = new FavoriteResponse();
        response.setSuccess(favorRecordRepository.existsByUser_UserIDAndRef_RefID(userId, refId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/classify/{userId}/{classify}")
    public ResponseEntity<FavoriteResponse> getFavoritesByClassify(
            @PathVariable Long userId,
            @PathVariable String classify) {

        FavoriteResponse response = new FavoriteResponse();

        // 验证用户存在
        if (!userRepository.existsById(userId)) {
            response.setSuccess(false);
            response.setMessage("用户不存在");
            return ResponseEntity.badRequest().body(response);
        }

        // 获取收藏的资源
        List<RefInfo> favorites = favorRecordRepository.findByUser_UserID(userId)
                .stream()
                .map(FavorRecord::getRef)
                .filter(ref -> classify.isEmpty() || ref.getClassification().equals(classify))
                .collect(Collectors.toList());

        // 获取收藏夹
        List<FavorFolder> folders = favorFolderRepository.findByUser_UserID(userId);

        response.setSuccess(true);
        response.setFavorites(favorites);
        response.setFolders(folders);
        return ResponseEntity.ok(response);
    }
}