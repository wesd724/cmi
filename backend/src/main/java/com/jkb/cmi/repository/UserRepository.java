package com.jkb.cmi.repository;

import com.jkb.cmi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
