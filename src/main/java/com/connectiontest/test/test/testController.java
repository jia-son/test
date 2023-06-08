package com.connectiontest.test.test;

import com.connectiontest.test.dto.request.MemberLoginReqDto;
import com.connectiontest.test.dto.request.MemberSignupReqDto;
import com.connectiontest.test.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * packageName    : com.member.member_jwt.controller
 * fileName       : testController
 * author         : wldk9
 * date           : 2023-06-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-08        wldk9       최초 생성
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class testController {

    private final testService testService;

    @PostMapping(value = "/join")
    public void join(@RequestBody MemberSignupReqDto memberSignupReqDto) {
        System.out.println(memberSignupReqDto);
        testService.join(memberSignupReqDto);
    }

    @PostMapping(value = "/user")
    public ResponseDto<?> login(@RequestBody @Valid MemberLoginReqDto memberLoginReqDto, HttpServletResponse response) {
        System.out.println("넌 뭐가 문제니?" + memberLoginReqDto);
        return testService.login(memberLoginReqDto, response);
    }

}
