package com.artistry.artistry.Service;

import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.oauth.OAuthProviderFactory;
import com.artistry.artistry.auth.oauth.SocialType;
import com.artistry.artistry.auth.oauth.endPoint.OAuthEndPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class OAuthServiceTest {


    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private OAuthProviderFactory oAuthProviderFactory;

    @Mock
    private OAuthClient oAuthClient;

    @Mock
    private OAuthEndPoint oAuthEndPoint;

    @InjectMocks
    private OAuthService oAuthService; // Assuming the class name is OAuthService

    @BeforeEach
    void setUp() {
        oAuthService = new OAuthService(memberRepository, jwtTokenProvider, oAuthProviderFactory);
    }

    @DisplayName("소셜 타입별로 로그인을 위한 링크를 생성한다.")
    @Test
    void createLoginLink() {
        SocialType socialType = SocialType.GOOGLE;
        String expectedUrl = "https://example.com/login";

        when(oAuthProviderFactory.createOAuthEndPoint(socialType)).thenReturn(oAuthEndPoint);
        when(oAuthEndPoint.generate()).thenReturn(expectedUrl);

        String actualUrl = oAuthService.generateLoginLink(socialType);

        assertEquals(expectedUrl, actualUrl);
    }


//    @DisplayName("이미 같은 이메일의 멤버가 저장되어 있지 않으면, 멤버를 등록한다.")
//    @Test
//    void testCreateMemberIfNotExists() {
//        OAuthMember oAuthMember = new OAuthMember("test@example.com", "testName", "testPictureUrl");
//
//        memberRepository.findByEmail()
//
//        oAuthService.createMemberIfNotExists(oAuthMember);
//
//        verify(memberRepository, times(1)).save(any(Member.class));
//    }
}
