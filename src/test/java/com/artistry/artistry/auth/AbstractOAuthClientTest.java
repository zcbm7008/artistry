package com.artistry.artistry.auth;

import com.artistry.artistry.auth.oauth.Client.AbstractOAuthClient;
import com.artistry.artistry.auth.oauth.OAuthMember;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AbstractOAuthClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;


    private AbstractOAuthClient abstractOAuthClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        abstractOAuthClient = new AbstractOAuthClientImpl(
                "http://localhost:8080/redirect",
                "client-id",
                "client-secret",
                "http://localhost:8080/token",
                restTemplate,
                objectMapper);
    }

    @DisplayName("AccessToken 요청을 수행한다.")
    @Test
    void testGetAccessToken() {
        // Given
        String code = "authorizationCode";

        TokenResponse tokenResponse =
                TokenResponse.builder()
                        .access_token("accessToken")
                        .refresh_token("refreshToken")
                        .token_type("bearer")
                        .expires_in("3600")
                        .build();

        ResponseEntity<TokenResponse> responseEntity = new ResponseEntity<>(tokenResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(eq("http://localhost:8080/token"), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenReturn(responseEntity);

        // When
        TokenResponse result = abstractOAuthClient.getAccessToken(code);

        // Then
        assertNotNull(result);
        assertEquals("accessToken", result.getAccess_token());
        assertEquals("refreshToken", result.getRefresh_token());
        assertEquals("bearer", result.getToken_type());
        assertEquals("3600", result.getExpires_in());
    }

    protected MultiValueMap<String, String> generateRequestParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", "client-id");
        params.add("client_secret", "client-secret");
        params.add("redirect_uri", "http://localhost:8080/redirect");
        params.add("grant_type", "authorization_code");
        return params;
    }

    // Concrete implementation of the abstract class for testing
    private static class AbstractOAuthClientImpl extends AbstractOAuthClient {
        public AbstractOAuthClientImpl(String redirectUri, String clientId, String clientSecret, String tokenUri, RestTemplate restTemplate, ObjectMapper objectMapper) {
            super(redirectUri, clientId, clientSecret, tokenUri, restTemplate, objectMapper);
        }
        @Override
        public OAuthMember createOAuthMember(TokenResponse tokenResponse) {
            // Implement this method as per your requirements
            return new OAuthMember("a@a","name","url");
        }
    }
}
