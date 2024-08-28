package com.company.adaptor.database.repository;

import com.company.domain.entity.Token;
import com.company.domain.entity.Token.Type;
import com.company.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ITokenRepository extends JpaRepository<Token, Integer> {

    @Modifying
    void deleteByUser(User user);

    Token findBykeyAndType(String key, Type type);
}
