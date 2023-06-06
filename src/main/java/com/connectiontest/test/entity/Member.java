package com.connectiontest.test.entity;

import javax.persistence.*;

import com.connectiontest.test.dto.request.NicknameUpdateRequestDto;
import com.connectiontest.test.dto.request.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * packageName    : com.connectiontest.test.entity
 * fileName       : Member
 * author         : son-jia
 * date           : 2023-06-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-01        son-jia       최초 생성
 */
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> postList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private RefreshToken refreshTokenList = new RefreshToken(

    );

    public void update(NicknameUpdateRequestDto nicknameUpdateRequestDto) {
        this.nickname = nicknameUpdateRequestDto.getNickname();
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    //추가된 내용
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
