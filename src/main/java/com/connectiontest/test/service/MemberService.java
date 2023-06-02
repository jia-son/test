package com.connectiontest.test.service;

import com.connectiontest.test.dto.request.LoginRequestDto;
import com.connectiontest.test.dto.request.MemberRequestDto;
import com.connectiontest.test.dto.response.MemberResponseDto;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.entity.Member;
import com.connectiontest.test.entity.Timestamped;
import com.connectiontest.test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * packageName    : com.connectiontest.test.service
 * fileName       : MemberService
 * author         : wldk9
 * date           : 2023-06-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-01        wldk9       최초 생성
 */
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Member isPresentMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 닉네임 입니다.");
        }

        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
                    "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        System.out.println("생성시간: " + member.getCreatedAt());
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto loginRequestDto) {
        Member member = isPresentMember(loginRequestDto.getNickname());
        if(member == null) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, loginRequestDto.getPassword())) {
            return ResponseDto.fail("INVALID_MEMBER", "비밀번호가 다릅니다.");
        }

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

//    public ResponseDto<?> delete(LoginRequestDto loginRequestDto) {
//        Member member = isPresentMember(loginRequestDto.getNickname());
//        if(member == null) {
//            return ResponseDto.fail("실패","계정이 존재하지 않습니다.");
//        }
//
//        memberRepository.delete(member);
//        return ResponseDto.success(
//                MemberResponseDto.builder()
//                        .id(member.getId())
//                        .nickname(member.getNickname())
//                        .createdAt(member.getCreatedAt())
//                        .modifiedAt(member.getModifiedAt())
//                        .build()
//        );
//    }
}
