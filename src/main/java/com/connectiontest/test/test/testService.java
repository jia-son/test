package com.connectiontest.test.test;

import com.connectiontest.test.domain.entity.Member;
import com.connectiontest.test.dto.request.MemberLoginReqDto;
import com.connectiontest.test.dto.request.MemberSignupReqDto;
import com.connectiontest.test.dto.response.MemberResDto;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.dto.response.TokenDto;
import com.connectiontest.test.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * packageName    : com.member.member_jwt.service
 * fileName       : testService
 * author         : wldk9
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        wldk9       최초 생성
 */
@RequiredArgsConstructor
@Service
public class testService {

    private final testRepository testRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public void join(MemberSignupReqDto memberSignupReqDto) {
        Member member = Member.builder()
                .memberId(memberSignupReqDto.getMemberId())
                .nickname(memberSignupReqDto.getNickname())
                .password(passwordEncoder.encode(memberSignupReqDto.getPassword()))
                .build();
        testRepository.save(member);
    }

    public ResponseDto<?> login(MemberLoginReqDto memberLoginReqDto, HttpServletResponse response) {

        System.out.println("진입했니?" + memberLoginReqDto);
        // 사용자로부터 입력받은 아이디를 통해 DB에 해당 아이디가 존재하는지 확인
        Member member = isPresentMember(memberLoginReqDto.getMemberId());
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }

        // 사용자로부터 입력받은 비밀번호와 DB상의 비밀번호가 일치하는지 확인
        if (!member.validatePassword(passwordEncoder, memberLoginReqDto.getPassword())) {
            return ResponseDto.fail("INVALID_PASSWORD", "패스워드가 일치하지 않습니다.");
        }

        // 위 과정을 모두 거치고 나면 정상적인 로그인으로 보고 토큰을 생성하여 헤더에 담아 보냄
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenProvider.tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResDto.builder()
                        .id(member.getId())
                        .memberId(member.getMemberId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }
    @Transactional(readOnly = true)
    public Member isPresentMember(String memberId) {
        Optional<Member> optionalMember = testRepository.findByMemberId(memberId);
        return optionalMember.orElse(null);
    }
}
