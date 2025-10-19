package com.bash.boundbackend.modules.auth.repository;

import com.bash.boundbackend.modules.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);
    @Query("""
    SELECT t FROM Token t
    WHERE t.user.id = :userId
    AND t.expiresAt > CURRENT_TIMESTAMP
    AND t.isUsed = false
    """)
    List<Token> findAllValidTokensByUser(@Param("userId") Integer userId);
}
