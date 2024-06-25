package com.artistry.artistry.auth;

import com.artistry.artistry.auth.oauth.endPoint.GoogleOAuthEndPoint;
import com.artistry.artistry.auth.oauth.endPoint.NaverOAuthEndPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class OAuthEndPointTest {

    private static final String GOOGLE_OAUTH_END_POINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final List<String> SCOPES = List.of("https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email");

    private static final String NAVER_OAUTH_END_POINT = "https://nid.naver.com/oauth2.0/authorize";
    private static final String STATE = System.getenv("oauth.naver.state");

    private String testRedirectUri;
    private String testClientId;

    private GoogleOAuthEndPoint googleOAuthEndPoint;
    private NaverOAuthEndPoint naverOAuthEndPoint;

    @BeforeEach
    public void setUp() {
        testRedirectUri = "googleRedirectUri";
        testClientId = "googleClientId";
        googleOAuthEndPoint = new GoogleOAuthEndPoint(testRedirectUri, testClientId);
        naverOAuthEndPoint = new NaverOAuthEndPoint(testRedirectUri, testClientId,STATE);
    }

    @DisplayName("Generate Google OAuth URL")
    @Test
    public void testGenerateGoogleUrl() {
        String expectedUrl = GOOGLE_OAUTH_END_POINT + "?"
                + "client_id=" + testClientId + "&"
                + "redirect_uri=" + testRedirectUri + "&"
                + "response_type=code&"
                + "scope=" + String.join(" ", SCOPES);

        String actualUrl = googleOAuthEndPoint.generate();

        assertThat(actualUrl).isEqualTo(expectedUrl);
    }

    @DisplayName("Generate Naver OAuth URL")
    @Test
    public void testGenerateNaverUrl() {
        String expectedUrl = NAVER_OAUTH_END_POINT + "?"
                + "client_id=" + testClientId + "&"
                + "redirect_uri=" + testRedirectUri + "&"
                + "response_type=" + "code" + "&"
                + "state=" + STATE;

        String actualUrl = naverOAuthEndPoint.generate();

        assertThat(actualUrl).isEqualTo(expectedUrl);
    }
}
