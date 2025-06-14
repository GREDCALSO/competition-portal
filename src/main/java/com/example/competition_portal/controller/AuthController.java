package com.example.competition_portal.controller;

import com.example.competition_portal.entity.UserInfo;
import com.example.competition_portal.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserInfo user) {
        Map<String, Object> response = new HashMap<>();
        logger.info("注册请求: {}", user.getUserName());

        // 验证用户名是否为空
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            logger.warn("注册失败: 用户名为空");
            response.put("success", false);
            response.put("message", "用户名不能为空");
            return toJsonResponse(response, HttpStatus.BAD_REQUEST);
        }

        // 验证密码是否为空
        if (user.getUserKeyword() == null || user.getUserKeyword().trim().isEmpty()) {
            logger.warn("注册失败: 密码为空");
            response.put("success", false);
            response.put("message", "密码不能为空");
            return toJsonResponse(response, HttpStatus.BAD_REQUEST);
        }

        // 检查用户名是否已存在
        if (userRepository.existsByUserName(user.getUserName())) {
            logger.warn("注册失败: 用户名已存在 - {}", user.getUserName());
            response.put("success", false);
            response.put("message", "用户名已存在");
            return toJsonResponse(response, HttpStatus.CONFLICT);
        }

        try {
            userRepository.save(user);
            logger.info("注册成功: {}", user.getUserName());
            response.put("success", true);
            response.put("message", "注册成功");
            return toJsonResponse(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("注册异常: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "注册失败: " + e.getMessage());
            return toJsonResponse(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserInfo user) {
        Map<String, Object> response = new HashMap<>();
        logger.info("登录请求: {}", user.getUserName());

        // 验证用户名是否为空
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            logger.warn("登录失败: 用户名为空");
            response.put("success", false);
            response.put("message", "用户名不能为空");
            return toJsonResponse(response, HttpStatus.BAD_REQUEST);
        }

        // 验证密码是否为空
        if (user.getUserKeyword() == null || user.getUserKeyword().trim().isEmpty()) {
            logger.warn("登录失败: 密码为空");
            response.put("success", false);
            response.put("message", "密码不能为空");
            return toJsonResponse(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Optional<UserInfo> existingUser = userRepository.findByUserName(user.getUserName());
            if (existingUser.isPresent()) {
                if (existingUser.get().getUserKeyword().equals(user.getUserKeyword())) {
                    logger.info("登录成功: {}", user.getUserName());
                    response.put("success", true);
                    response.put("message", "登录成功");

                    // 用户信息
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("userID", existingUser.get().getUserID());
                    userData.put("userName", existingUser.get().getUserName());
                    response.put("user", userData);

                    return toJsonResponse(response, HttpStatus.OK);
                } else {
                    logger.warn("密码错误: {}", user.getUserName());
                    response.put("success", false);
                    response.put("message", "密码错误");
                    return toJsonResponse(response, HttpStatus.UNAUTHORIZED);
                }
            } else {
                logger.warn("用户不存在: {}", user.getUserName());
                response.put("success", false);
                response.put("message", "用户不存在");
                return toJsonResponse(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("登录异常: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "登录失败: " + e.getMessage());
            return toJsonResponse(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<String> toJsonResponse(Map<String, Object> response, HttpStatus status) {
        try {
            String json = objectMapper.writeValueAsString(response);
            return ResponseEntity.status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        } catch (JsonProcessingException e) {
            logger.error("JSON序列化失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"success\":false,\"message\":\"服务器内部错误\"}");
        }
    }
}