package com.neu.boke2.controller;

import com.neu.boke2.entity.User;
import com.neu.boke2.security.JwtUtil;
import com.neu.boke2.service.UserService;
import com.neu.boke2.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(username);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody User user) {
        // 校验用户名和邮箱唯一性
        if (userService.findByUsername(user.getUsername()) != null) {
            return ApiResponse.error("用户名已存在");
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            return ApiResponse.error("邮箱已存在");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        User saved = userService.save(user);
        return ApiResponse.success(saved);
    }

    @GetMapping("/me")
    public ApiResponse<User> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.success(userService.findByUsername(username));
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        SecurityContextHolder.clearContext();
        Map<String, Object> result = new HashMap<>();
        result.put("message", "已退出登录");
        return result;
    }
} 