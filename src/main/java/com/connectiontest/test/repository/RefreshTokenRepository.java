package com.connectiontest.test.repository;

import com.connectiontest.test.entity.Member;
import com.connectiontest.test.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.connectiontest.test.repository
 * fileName       : RefreshTokenRepository
 * author         : wldk9
 * date           : 2023-06-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-06        wldk9       최초 생성
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
