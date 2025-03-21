package com.jkb.cmi.user.repository;

import com.jkb.cmi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password); // 임시
    User getByUsername(String username);
    Boolean existsByUsername(String username);
}
