package com.connectiontest.test.controller;

import com.connectiontest.test.dto.request.LoginRequestDto;
import com.connectiontest.test.dto.request.MemberRequestDto;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
public class MemberController {
    private final MemberService memberService;

    @RequestMapping(value = "api/member/signup", method = RequestMethod.POST)
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return memberService.createMember(memberRequestDto);
    }

    @RequestMapping(value = "api/member/login", method = RequestMethod.POST)
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return memberService.login(loginRequestDto);
    }
}
