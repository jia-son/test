package com.connectiontest.test.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * packageName    : com.connectiontest.test.dto.request
 * fileName       : NicknameUpdateRequestDto
 * author         : wldk9
 * date           : 2023-06-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-07        wldk9       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameUpdateRequestDto {
    @NotBlank
    @Size(min = 1, max = 10)
    @Pattern(regexp = "[a-zA-Z가-힣\\d]*${0,12}")
    private String nickname;
}
