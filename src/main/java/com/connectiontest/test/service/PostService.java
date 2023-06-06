package com.connectiontest.test.service;

import com.connectiontest.test.dto.request.PostRequestDto;
import com.connectiontest.test.dto.response.GetAllPostResponseDto;
import com.connectiontest.test.dto.response.MemberResponseDto;
import com.connectiontest.test.dto.response.PostResponseDto;
import com.connectiontest.test.dto.response.ResponseDto;
import com.connectiontest.test.entity.Member;
import com.connectiontest.test.entity.Post;
import com.connectiontest.test.jwt.TokenProvider;
import com.connectiontest.test.repository.MemberRepository;
import com.connectiontest.test.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.connectiontest.test.service
 * fileName       : PostService
 * author         : wldk9
 * date           : 2023-06-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-07        wldk9       최초 생성
 */
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    public ResponseDto<?> createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = Post.builder()
                .category(postRequestDto.getCategory())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .completion(postRequestDto.getCompletion())
                .member(member)
                .build();

        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .category(post.getCategory())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .completion(post.getCompletion())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    public ResponseDto<?> updatePost(Long postId, PostRequestDto postRequestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        post.update(postRequestDto);
        postRepository.save(post);

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .category(post.getCategory())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .completion(post.getCompletion())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    public ResponseDto<?> deletePost(Long postId, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
        return ResponseDto.success("delete success");
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPosts(HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        List<Post> allPosts = postRepository.findAllByMemberIdOrderByModifiedAtDesc(member.getId());
        List<GetAllPostResponseDto> getAllPostResponseDtoList = new ArrayList<>();

        for(Post post : allPosts) {
            getAllPostResponseDtoList.add(
                    GetAllPostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .nickname(post.getMember().getNickname())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(getAllPostResponseDtoList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long postId, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .nickname(post.getMember().getNickname())
                        .category(post.getCategory())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .completion(post.getCompletion())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    public ResponseDto<?> getCategoryPost(String category, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        List<Post> allPosts = postRepository.findAllByCategoryOrderByModifiedAtDesc(category);
        List<GetAllPostResponseDto> getAllPostResponseDtoList = new ArrayList<>();

        for(Post post : allPosts) {
            getAllPostResponseDtoList.add(
                    GetAllPostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .nickname(post.getMember().getNickname())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(getAllPostResponseDtoList);
    }
}
