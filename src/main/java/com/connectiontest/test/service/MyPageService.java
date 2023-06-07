package com.connectiontest.test.service;

import com.connectiontest.test.dto.response.MyPageResponseDto;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.entity.Member;
import com.connectiontest.test.jwt.TokenProvider;
import com.connectiontest.test.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * packageName    : com.connectiontest.test.service
 * fileName       : MyPageService
 * author         : wldk9
 * date           : 2023-06-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-07        wldk9       최초 생성
 */
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        if (null == request.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Long totalPostCount = postRepository.countBy();
        Long completPostCount = postRepository.countByCompletionGreaterThanEqual(1);
        Long incompletPostCount = totalPostCount - completPostCount;

        return ResponseDto.success(
                MyPageResponseDto.builder()
                        .nickname(member.getNickname())
                        .totalPostCount(totalPostCount)
                        .completPost(completPostCount)
                        .incompletPost(incompletPostCount)
                        .build()
        );
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
