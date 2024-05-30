package com.artistry.artistry.auth;

import com.artistry.artistry.auth.oauth.Client.NaverOAuthClient;
import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.oauth.OAuthProviderFactoryImpl;
import com.artistry.artistry.auth.oauth.SocialType;
import com.artistry.artistry.auth.oauth.endPoint.NaverOAuthEndPoint;
import com.artistry.artistry.auth.oauth.endPoint.OAuthEndPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class OAuthFactoryTest {
    @Autowired
    private OAuthProviderFactoryImpl oAuthProviderFactory;

    @DisplayName("SocialType에 따라 객체를 생성한다.")
    @Test
    void createNaverProvider(){
        SocialType socialType = SocialType.NAVER;

        OAuthClient oAuthClient = oAuthProviderFactory.createOAuthClient(socialType);
        OAuthEndPoint oAuthEndPoint = oAuthProviderFactory.createOAuthEndPoint(socialType);

        assertThat(oAuthClient).isInstanceOf(NaverOAuthClient.class);
        assertThat(oAuthEndPoint).isInstanceOf(NaverOAuthEndPoint.class);

    }

    @DisplayName("SocialType이 없을 때")
    @Nested
    class illegalSocialType{
        @DisplayName("클라이언트 생성시 예외를 출력한다.")
        @Test
        void onCreateClient(){
            assertThatThrownBy(() -> oAuthProviderFactory.createOAuthClient(SocialType.valueOf("novalue")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
        @DisplayName("엔드포인트 생성시 예외를 출력한다.")
        @Test
        void onCreateEndpoint(){
            assertThatThrownBy(() -> oAuthProviderFactory.createOAuthEndPoint(SocialType.valueOf("novalue")))
                    .isInstanceOf(IllegalArgumentException.class);
        }


    }


}
