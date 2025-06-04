package com.neu.boke2.service;

import com.neu.boke2.entity.User;
import com.neu.boke2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public long count() {
        return userRepository.count();
    }

    public long countByIsActive(boolean isActive) {
        return userRepository.countByIsActive(isActive);
    }

    public long countByRole(User.Role role) {
        return userRepository.countByRole(role);
    }

    public Page<User> findByKeyword(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        }

        // 创建动态查询条件
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 用户名模糊查询
            predicates.add(cb.like(root.get("username"), "%" + keyword.trim() + "%"));

            // 邮箱模糊查询
            predicates.add(cb.like(root.get("email"), "%" + keyword.trim() + "%"));

            // 显示名称模糊查询
            predicates.add(cb.like(root.get("displayName"), "%" + keyword.trim() + "%"));

            return cb.or(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, pageable);
    }
}