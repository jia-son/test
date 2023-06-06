package com.connectiontest.test.controller;

import com.connectiontest.test.dto.request.MemberRequestDto;
import com.connectiontest.test.dto.request.PostRequestDto;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.service.MemberService;
import com.connectiontest.test.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * packageName    : com.connectiontest.test.controller
 * fileName       : PostController
 * author         : wldk9
 * date           : 2023-06-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-07        wldk9       최초 생성
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/post")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/create")
    public ResponseDto<?> createPost(@RequestBody @Valid PostRequestDto postRequestDto, HttpServletRequest request) {
        return postService.createPost(postRequestDto, request);
    }

    @PutMapping(value = "/update/{postId}")
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequestDto postRequestDto, HttpServletRequest request) {
        return postService.updatePost(postId, postRequestDto, request);
    }

    @DeleteMapping(value = "/delete/{postId}")
    public ResponseDto<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        return postService.deletePost(postId, request);
    }

    @GetMapping(value = "/list")
    public ResponseDto<?> getAllPosts(HttpServletRequest request) {
        return postService.getAllPosts(request);
    }

    @GetMapping(value = "/detail/{postId}")
    public ResponseDto<?> getPost(@PathVariable Long postId, HttpServletRequest request) {
        return postService.getPost(postId, request);
    }

    @GetMapping(value = "/list/{category}")
    public ResponseDto<?> getCategoryPost(@PathVariable String category, HttpServletRequest request) {
        return postService.getCategoryPost(category, request);
    }
}
