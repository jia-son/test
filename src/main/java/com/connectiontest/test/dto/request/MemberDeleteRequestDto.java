package com.connectiontest.test.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * packageName    : com.connectiontest.test.dto.request
 * fileName       : MemberDeleteRequestDto
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
public class MemberDeleteRequestDto {

    @NotBlank
    @Size(min = 4, max = 32)
    @Pattern(regexp = "[a-z\\d]*${3,32}")
    private String password;
}
