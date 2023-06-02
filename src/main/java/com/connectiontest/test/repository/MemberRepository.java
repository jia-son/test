package com.connectiontest.test.repository;

import com.connectiontest.test.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.connectiontest.test.repository
 * fileName       : MemberRepository
 * author         : wldk9
 * date           : 2023-06-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-01        wldk9       최초 생성
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
}
