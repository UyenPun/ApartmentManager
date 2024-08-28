package com.company.adaptor.database.repository;

import com.company.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IUserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByUsername(String username); // tìm kiếm người dùng theo tên đăng nhập

    boolean existsByUsername(String username);

}
