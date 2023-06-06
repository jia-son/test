package com.connectiontest.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.connectiontest.test.controller
 * fileName       : HelloWorldController
 * author         : wldk9
 * date           : 2023-06-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-06        wldk9       최초 생성
 */
@RestController
public class HelloWorldController {

    @GetMapping("/api/hello")
    public String test() {
        return "Hello, world!";
    }
}
