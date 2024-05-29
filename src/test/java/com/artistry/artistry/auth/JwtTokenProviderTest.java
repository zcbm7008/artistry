package com.artistry.artistry.auth;

import com.artistry.artistry.auth.properties.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("엑세스 토큰 생성을 성공한다.")
    @Test
    void generateAccessToken() {
        //given
        String email = "aa@a.com";

        //when
        String accessToken = jwtTokenProvider.generateToken(email);

        //then
        assertThat(accessToken).isNotNull();
    }

    @DisplayName("엑세스 토큰에서 이메일을 추출한다.")
    @Test
    void extractEmailFromAccessToken() {
        //given
        String expectedEmail = "ab@c.com";
        String accessToken = jwtTokenProvider.generateToken(expectedEmail);

        //when
        String email = jwtTokenProvider.extractEamilFromToken(accessToken);

        //then
        assertThat(expectedEmail).isEqualTo(email);
    }
}
