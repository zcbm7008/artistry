package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.oauth.OAuthMemberResponse;
import com.artistry.artistry.auth.oauth.OAuthProviderFactory;
import com.artistry.artistry.auth.oauth.SocialType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class OAuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @Autowired
    private OAuthProviderFactory oAuthProviderFactory;
    @Mock
    private OAuthClient oAuthClient;

    @Autowired
    private OAuthService oAuthService;


    @DisplayName("provider에 따라 SocialType을 리턴한다.")
    @Test
    void getSocialType() {
        String provider = "naver";
        SocialType socialType = oAuthService.getSocialType(provider);
        assertThat(socialType).isNotNull();
    }

    @DisplayName("SocialType의 값이 적절하지 않으면 예외를 던진다.")
    @Test
    void socialTypeException() {
        String provider = "asdgjksdajgksdajkgsdagsdag";
        assertThatThrownBy(() -> oAuthService.getSocialType(provider))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("소셜 타입별로 로그인을 위한 링크를 생성한다.")
    @Test
    void createLoginLink() {
        SocialType socialType = SocialType.GOOGLE;
        String actualUrl = oAuthService.generateLoginLink(socialType);

        assertThat(actualUrl).isNotNull();
    }

    @Test
    public void testCreateMemberAccessToken() throws JsonProcessingException {
        String code = "authorization_code";
        TokenResponse tokenResponse = TokenResponse.builder().access_token("access_token").build();
        OAuthMemberResponse oAuthMember = new OAuthMemberResponse("nickname", "email@example.com", "image.url");

        when(oAuthClient.getAccessToken(eq(code))).thenReturn(tokenResponse);
        when(oAuthClient.createOAuthMember(eq(tokenResponse))).thenReturn(oAuthMember);
        when(jwtTokenProvider.generateEmailToken(eq("email@example.com"))).thenReturn("jwt_token");

        AccessTokenResponse accessTokenResponse = oAuthService.createMemberAccessToken(SocialType.GOOGLE, code);

        assertEquals("jwt_token", accessTokenResponse.getAccessToken());
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}
