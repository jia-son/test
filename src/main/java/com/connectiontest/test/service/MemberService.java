package com.connectiontest.test.service;

import com.connectiontest.test.domain.entity.Member;
import com.connectiontest.test.dto.request.MemberDeleteReqDto;
import com.connectiontest.test.dto.request.MemberLoginReqDto;
import com.connectiontest.test.dto.request.MemberSignupReqDto;
import com.connectiontest.test.dto.response.MemberResDto;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.dto.response.TokenDto;
import com.connectiontest.test.jwt.TokenProvider;
import com.connectiontest.test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseDto<?> createMember(MemberSignupReqDto memberSignupReqDto) {
        // 사용자로부터 입력받은 memberId로 DB에 같은 아이디가 있는지 확인
        if (null != isPresentMember(memberSignupReqDto.getMemberId())) {
            return ResponseDto.fail("DUPLICATED_ID","중복된 아이디 입니다.");
        }

        // 비밀번호와 비밀번호 확인의 값이 서로 일치하는지 확인
        if (!memberSignupReqDto.getPassword().equals(memberSignupReqDto.getPasswordConfirm())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .memberId(memberSignupReqDto.getMemberId())
                .nickname(memberSignupReqDto.getNickname())
                .password(passwordEncoder.encode(memberSignupReqDto.getPassword()))
                .build();
        memberRepository.save(member);
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

    // 로그인 로직
    @Transactional
    public ResponseDto<?> login(MemberLoginReqDto memberLoginReqDto, HttpServletResponse response) {
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

    // 로그아웃 로직
//    @Transactional
//    public ResponseDto<?> logout(HttpServletRequest request) {
//        // 토큰 유효성 검증
//        if (!tokenProvider.validateToken(tokenProvider.resolveToken(request))) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }
//
//        // 토큰을 통해 실제 사용자가 DB상에 존재하는지 확인
//        Member member = tokenProvider.getMemberFromAuthentication();
//        if (null == member) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "사용자를 찾을 수 없습니다.");
//        }
//
//        /* *
//        * 위 과정을 다 통과하면 DB에 저장되어 있는 토큰을 삭제
//        * 토큰이 삭제되면 다시 로그인을 해 토큰을 새로 발급받기 전까지는 사용자에게 권한이 없어진다.
//        * */
//        return tokenProvider.deleteRefreshToken(member);
//    }
    @Transactional
    public ResponseDto<?> logout(HttpServletRequest request) {
         //토큰 유효성 검증
        if (!tokenProvider.validateToken(tokenProvider.resolveToken(request))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 토큰을 통해 실제 사용자가 DB상에 존재하는지 확인
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        /* *
         * 위 과정을 다 통과하면 DB에 저장되어 있는 토큰을 삭제
         * 토큰이 삭제되면 다시 로그인을 해 토큰을 새로 발급받기 전까지는 사용자에게 권한이 없어진다.
         * */
        return tokenProvider.deleteRefreshToken(member);
    }

    // 회원 탈퇴 로직
    @Transactional
    public ResponseDto<?> delete(MemberDeleteReqDto memberDeleteReqDto, HttpServletRequest request) {
        // 토큰 유효성 검증 // 다시 살려야함
        if (!tokenProvider.validateToken(tokenProvider.resolveToken(request))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 토큰을 통해 실제 사용자가 DB상에 존재하는지 확인
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        // 사용자로부터 입력받은 비밀번호와 DB상의 비밀번호가 일치하는지 확인
        if (!member.validatePassword(passwordEncoder, memberDeleteReqDto.getPassword())) {
            return ResponseDto.fail("INVALID_PASSWORD", "패스워드가 일치하지 않습니다.");
        }

        // 위 과정을 다 통과하면 DB상에서 사용자를 삭제함 cascade설정을 해뒀기때문에 토큰은 자동적으로 삭제됨
        memberRepository.delete(member);

        return ResponseDto.success("정상적으로 탈퇴되었습니다.");
    }

    // 파라미터로 넘어온 memberId를 이용해 DB에서 memberId를 갖고 있는 데이터가 있는지 확인함
    @Transactional(readOnly = true)
    public Member isPresentMember(String memberId) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
        return optionalMember.orElse(null);
    }
}
