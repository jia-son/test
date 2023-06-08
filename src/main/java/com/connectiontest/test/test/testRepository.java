package com.connectiontest.test.test;
import com.connectiontest.test.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * packageName    : com.member.member_jwt.repository
 * fileName       : testRepository
 * author         : wldk9
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        wldk9       최초 생성
 */
public interface testRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
}
