package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.company.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);

	boolean existsByUsername(String username);
}
