package com.artistry.artistry.Service;

import com.artistry.artistry.auth.oauth.OAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private OAuthService oAuthService;

    @DisplayName("구글 로그인을 위한 링크를 생성한다.")
    @Test
    void 구글_로그인을_위한_링크를_생성한다() {
        // given
        String link = oAuthService.generateLoginLink();
        System.out.println(link);


        // when & then
        assertThat(link).isNotEmpty();
    }

//    @DisplayName("토큰 생성을 하면 OAuth 서버에서 인증 후 토큰을 반환한다")
//    @Test
//    void 토큰_생성을_하면_OAuth_서버에서_인증_후_토큰을_반환한다() {
//        // given
//        String code = "authorization code";
//
//        // when
//        TokenResponse actual = oAuthService.generateTokenWithCode(code);
//
//        // then
//        assertThat(actual.getAccessToken()).isNotEmpty();
//    }
}
