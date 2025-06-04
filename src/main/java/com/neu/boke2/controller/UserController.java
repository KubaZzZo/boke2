package com.neu.boke2.controller;

import com.neu.boke2.common.ApiResponse;
import com.neu.boke2.entity.User;
import com.neu.boke2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getUserStats() {
        Map<String, Object> stats = new HashMap<>();

        // 获取总用户数
        stats.put("totalCount", userService.count());

        // 获取活跃用户数
        stats.put("activeCount", userService.countByIsActive(true));

        // 获取不同角色的用户数
        stats.put("adminCount", userService.countByRole(User.Role.admin));
        stats.put("editorCount", userService.countByRole(User.Role.editor));
        stats.put("subscriberCount", userService.countByRole(User.Role.subscriber));

        return ApiResponse.success(stats);
    }

    @GetMapping("/page")
    public ApiResponse<Page<User>> getPagedUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> users = userService.findByKeyword(keyword, pageRequest);
        return ApiResponse.success(users);
    }

    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success(userService.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ApiResponse::success)
                .orElseGet(() -> ApiResponse.error("用户不存在"));
    }

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userService.findByUsername(user.getUsername()) != null) {
            return ApiResponse.error("用户名已存在");
        }
        // 检查邮箱是否已存在
        if (userService.findByEmail(user.getEmail()) != null) {
            return ApiResponse.error("邮箱已存在");
        }
        return ApiResponse.success(userService.save(user));
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        // 获取原用户信息
        User existingUser = userService.findById(id)
                .orElse(null);
        if (existingUser == null) {
            return ApiResponse.error("用户不存在");
        }

        // 检查用户名唯一性
        User userWithSameUsername = userService.findByUsername(user.getUsername());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(id)) {
            return ApiResponse.error("用户名已存在");
        }

        // 检查邮箱唯一性
        User userWithSameEmail = userService.findByEmail(user.getEmail());
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(id)) {
            return ApiResponse.error("邮箱已存在");
        }

        // 设置必要字段
        user.setId(id);
        user.setPasswordHash(existingUser.getPasswordHash()); // 保持原密码
        user.setCreatedAt(existingUser.getCreatedAt()); // 保持创建时间

        return ApiResponse.success(userService.save(user));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            return ApiResponse.error("用户不存在");
        }
        userService.deleteById(id);
        return ApiResponse.success(null);
    }
}