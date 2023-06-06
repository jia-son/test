package com.connectiontest.test.repository;

import com.connectiontest.test.entity.Member;
import com.connectiontest.test.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.connectiontest.test.repository
 * fileName       : PostRepository
 * author         : wldk9
 * date           : 2023-06-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-07        wldk9       최초 생성
 */
public interface PostRepository extends JpaRepository<Post, Long> {
}
