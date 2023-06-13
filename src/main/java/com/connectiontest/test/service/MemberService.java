package com.connectiontest.test.service;

import com.connectiontest.test.domain.entity.Member;
import com.connectiontest.test.dto.request.MemberDeleteReqDto;
import com.connectiontest.test.dto.request.MemberLoginReqDto;
import com.connectiontest.test.dto.request.MemberSignupReqDto;
import com.connectiontest.test.dto.response.TokenDto;
import com.connectiontest.test.jwt.TokenProvider;
import com.connectiontest.test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * packageName    : com.member.member_jwt.service
 * fileName       : MemberService
 * author         : sonjia
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        sonjia       최초 생성
 *                                실질적인 비즈니스 로직이 처리되는 클래스
 *                                추가적으로 @Transactional이 붙은 메서드는
 *                                메서드가 포함하고 있는 작업 중에 하나라도 실패할 경우 전체 작업을 취소한다.
 */
@RequiredArgsConstructor
@Service
public class MemberService {

    // DB와 소통해야하므로 repository 선언
    private final MemberRepository memberRepository;
    // 비밀번호의 암호화를 위해 선언
    private final PasswordEncoder passwordEncoder;
    // jwt 사용을 위해 선언
    private final TokenProvider tokenProvider;

    // 회원가입 로직
    @Transactional
    public ResponseEntity<?> createMember(MemberSignupReqDto memberSignupReqDto) {
        // 사용자로부터 입력받은 memberId로 DB에 같은 아이디가 있는지 확인
        if (null != isPresentMember(memberSignupReqDto.getMemberId())) {
            return ResponseEntity.ok("DUPLICATED_ID");
        }

        // 비밀번호와 비밀번호 확인의 값이 서로 일치하는지 확인
        if (!memberSignupReqDto.getPassword().equals(memberSignupReqDto.getPasswordConfirm())) {
            return ResponseEntity.ok("PASSWORDS_NOT_MATCHED");
        }

        Member member = Member.builder()
                .memberId(memberSignupReqDto.getMemberId())
                .nickname(memberSignupReqDto.getNickname())
                .password(passwordEncoder.encode(memberSignupReqDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인 로직
    @Transactional
    public ResponseEntity<?> login(MemberLoginReqDto memberLoginReqDto, HttpServletResponse response) {
        // 사용자로부터 입력받은 아이디를 통해 DB에 해당 아이디가 존재하는지 확인
        Member member = isPresentMember(memberLoginReqDto.getMemberId());
        if (null == member) {
            return ResponseEntity.ok("MEMBER_NOT_FOUND");
        }

        // 사용자로부터 입력받은 비밀번호와 DB상의 비밀번호가 일치하는지 확인
        if (!member.validatePassword(passwordEncoder, memberLoginReqDto.getPassword())) {
            return ResponseEntity.ok("INVALID_PASSWORD");
        }

        // 위 과정을 모두 거치고 나면 정상적인 로그인으로 보고 토큰을 생성하여 헤더에 담아 보냄
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenProvider.tokenToHeaders(tokenDto, response);
        return ResponseEntity.ok("로그인 성공");
    }

    // 로그아웃 로직
    @Transactional
    public ResponseEntity<?> logout(Member member) {
        tokenProvider.deleteRefreshToken(member);
        System.out.println("삭제 완료");
        return ResponseEntity.ok("로그아웃 성공");
    }

    // 회원 탈퇴 로직
    @Transactional
    public ResponseEntity<?> delete(MemberDeleteReqDto memberDeleteReqDto, Member member) {
        if (!member.validatePassword(passwordEncoder, memberDeleteReqDto.getPassword())) {
            return ResponseEntity.ok("INVALID_PASSWORD");
        }
        memberRepository.delete(member);
        return ResponseEntity.ok("회원탈퇴 성공");
    }

    // 파라미터로 넘어온 memberId를 이용해 DB에서 memberId를 갖고 있는 데이터가 있는지 확인함
    @Transactional(readOnly = true)
    public Member isPresentMember(String memberId) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
        return optionalMember.orElse(null);
    }
}
