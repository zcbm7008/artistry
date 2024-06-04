package com.artistry.artistry.Service;

import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.oauth.OAuthMemberResponse;
import com.artistry.artistry.auth.oauth.OAuthProviderFactory;
import com.artistry.artistry.auth.oauth.SocialType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest
public class OAuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private MemberService memberService;


    @Mock
    private OAuthProviderFactory oAuthProviderFactory;
    @Mock
    private OAuthClient oAuthClient;

    @Autowired
    private OAuthService oAuthService;
    @InjectMocks
    private OAuthService mockOAuthService;

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

    @DisplayName("소셜 타입과 코드를 받아 멤버 이메일 Jwt 토큰을 생성한다.")
    @Test
    public void testCreateMemberAccessToken() throws JsonProcessingException {
        // Given
        SocialType socialType = SocialType.GOOGLE;
        String code = "authorization_code";
        TokenResponse tokenResponse = TokenResponse.builder().access_token("access_token").build();
        OAuthMemberResponse oAuthMemberResponse = new OAuthMemberResponse("nickname", "email@example.com", "image.url");
        AccessTokenResponse expectedAccessTokenResponse = new AccessTokenResponse("access_token");

        // Mock dependencies
        OAuthClient mockOAuthClient = mock(OAuthClient.class);
        when(oAuthProviderFactory.createOAuthClient(eq(socialType))).thenReturn(mockOAuthClient);
        when(mockOAuthClient.getAccessToken(eq(code))).thenReturn(tokenResponse);
        when(mockOAuthClient.createOAuthMember(eq(tokenResponse))).thenReturn(oAuthMemberResponse);
        when(jwtTokenProvider.generateEmailToken(eq("email@example.com"))).thenReturn("jwt_token");
        when(memberService.findByEmail(any(String.class))).thenReturn(null);

        // When
        AccessTokenResponse actualAccessTokenResponse = mockOAuthService.createMemberAccessToken(socialType, code);

        // Then
        verify(oAuthProviderFactory, times(1)).createOAuthClient(eq(socialType));
        verify(mockOAuthClient, times(1)).getAccessToken(eq(code));
        verify(mockOAuthClient, times(1)).createOAuthMember(eq(tokenResponse));
        verify(jwtTokenProvider, times(1)).generateEmailToken(eq("email@example.com"));
        verify(memberService, times(1)).create(any(MemberCreateRequest.class));
    }

    @DisplayName("같은 이메일의 유저가 있을 경우 멤버를 생성하지 않음.")
    @Test
    public void testCreate() throws JsonProcessingException {
        // Given
        SocialType socialType = SocialType.GOOGLE;
        String code = "authorization_code";
        TokenResponse tokenResponse = TokenResponse.builder().access_token("access_token").build();
        OAuthMemberResponse oAuthMemberResponse = new OAuthMemberResponse("nickname", "email@example.com", "image.url");
        AccessTokenResponse expectedAccessTokenResponse = new AccessTokenResponse("access_token");

        // Mock dependencies
        OAuthClient mockOAuthClient = mock(OAuthClient.class);
        when(oAuthProviderFactory.createOAuthClient(eq(socialType))).thenReturn(mockOAuthClient);
        when(mockOAuthClient.getAccessToken(eq(code))).thenReturn(tokenResponse);
        when(mockOAuthClient.createOAuthMember(eq(tokenResponse))).thenReturn(oAuthMemberResponse);
        when(jwtTokenProvider.generateEmailToken(eq("email@example.com"))).thenReturn("jwt_token");
        when(memberService.findByEmail(any(String.class))).thenReturn(new MemberResponse(0L,"member1","a.url","email@example.com"));

        // When,Then

        AccessTokenResponse actualAccessTokenResponse = mockOAuthService.createMemberAccessToken(socialType, code);

        // Then

        verify(memberService, times(0)).create(any(MemberCreateRequest.class));
    }
}
