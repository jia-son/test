package com.connectiontest.test.controller;

import com.connectiontest.test.dto.request.LoginRequestDto;
import com.connectiontest.test.dto.request.MemberDeleteRequestDto;
import com.connectiontest.test.dto.request.MemberRequestDto;
import com.connectiontest.test.dto.request.NicknameUpdateRequestDto;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * packageName    : com.connectiontest.test.controller
 * fileName       : MemberController
 * author         : wldk9
 * date           : 2023-06-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-01        wldk9       최초 생성
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/signup")
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    @PostMapping(value = "/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    @GetMapping(value = "/logout")
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }

    @PostMapping(value = "/delete")
    public ResponseDto<?> delete(@RequestBody @Valid MemberDeleteRequestDto memberDeleteRequestDto, HttpServletRequest request) {
        return memberService.delete(memberDeleteRequestDto, request);
    }

    //PUTT으로 처리할 수 있을 듯하여 진행해봄
    @PutMapping(value = "/update/nickname")
    public ResponseDto<?> updateNickname(@RequestBody @Valid NicknameUpdateRequestDto nicknameUpdateRequestDto, HttpServletRequest request) {
        return memberService.updateNickname(nicknameUpdateRequestDto, request);
    }

    //재발급
      @RequestMapping(value = "/api/auth/member/reissue", method = RequestMethod.POST)
      public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return memberService.reissue(request, response);
      }
}
