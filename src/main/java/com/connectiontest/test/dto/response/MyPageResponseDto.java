package com.connectiontest.test.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.connectiontest.test.dto.response
 * fileName       : MypageResponseDto
 * author         : wldk9
 * date           : 2023-06-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-07        wldk9       최초 생성
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private String nickname;
    private Long totalPostCount;
    private Long completPost;
    private Long incompletPost;
}
