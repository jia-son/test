package com.connectiontest.test.service;

import com.connectiontest.test.domain.entity.Member;
import com.connectiontest.test.domain.entity.RefreshToken;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.dto.response.TokenDto;
import com.connectiontest.test.jwt.TokenProvider;
import com.connectiontest.test.repository.MemberRepository;
import com.connectiontest.test.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * packageName    : com.member.member_jwt.service
 * fileName       : JwtService
 * author         : sonjia
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        sonjia       최초 생성
 *                                토큰 재발급을 받기 위한 service
 */
@RequiredArgsConstructor
@Service
public class JwtService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    // 토큰 재발급 로직
    @Transactional
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 엑세스 토큰안에 담긴 정보를 이용해 DB상에 실제 사용자가 존재하는지 확인
        String accessToken = tokenProvider.resolveToken(request);
        Member member = memberRepository.findByMemberId(tokenProvider.getClaimsMemberId(accessToken)).get();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        // 리프레시 토큰 유효성 검증
        RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);
        if (!refreshToken.getValue().equals(request.getHeader("RefreshToken"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 위 과정을 다 통과하면 새로운 토큰을 만들어 response에 담아 보냄
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        // 리프레시 토큰도 새로 발급해 DB에 저장
        refreshToken.updateValue(tokenDto.getRefreshToken());
        tokenProvider.tokenToHeaders(tokenDto, response);

        return ResponseDto.success("success");
    }
}
