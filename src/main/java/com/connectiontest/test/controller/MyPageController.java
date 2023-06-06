package com.connectiontest.test.controller;

import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * packageName    : com.connectiontest.test.controller
 * fileName       : MyPageController
 * author         : wldk9
 * date           : 2023-06-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-07        wldk9       최초 생성
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping(value = "/mypage")
    public ResponseDto<?> getMyPage(HttpServletRequest request) {
        return myPageService.getMyPage(request);
    }
}
