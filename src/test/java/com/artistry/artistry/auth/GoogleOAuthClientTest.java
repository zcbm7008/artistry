package com.artistry.artistry.auth;

import com.artistry.artistry.auth.oauth.Client.GoogleOAuthClient;
import com.artistry.artistry.auth.oauth.OAuthMemberResponse;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GoogleOAuthClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GoogleOAuthClient googleOAuthClient;

    @BeforeEach
    void setUp() {
        googleOAuthClient = new GoogleOAuthClient(
                "redirectUri",
                "clientId",
                "clientSecret",
                "tokenUri",
                restTemplate,
                objectMapper
        );
    }

    @Test
    void testCreateOAuthMember() throws JsonProcessingException {
        // Mocking the TokenResponse

        String idToken = generateIdToken("test@example.com", "testName", "testPictureUrl");
        TokenResponse tokenResponse =
                TokenResponse.builder()
                        .id_token(idToken)
                        .build();
        // Call the method to test
        OAuthMemberResponse oAuthMemberResponse = googleOAuthClient.createOAuthMember(tokenResponse);

        // Verify the results
        assertEquals("test@example.com", oAuthMemberResponse.getEmail());
        assertEquals("testName", oAuthMemberResponse.getNickName());
        assertEquals("testPictureUrl", oAuthMemberResponse.getProfileImageUrl());
    }

    private String generateIdToken(String email, String name, String picture) throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("name", name);
        payload.put("picture", picture);

        String payloadJson = new ObjectMapper().writeValueAsString(payload);
        String encodedPayload = Base64.getEncoder().encodeToString(payloadJson.getBytes());

        // We are not concerned with header and signature for this test
        return "header." + encodedPayload + ".signature";
    }
}
