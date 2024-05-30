package com.artistry.artistry.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import com.artistry.artistry.Exceptions.ArtistryOAuthException;
import com.artistry.artistry.auth.oauth.Client.NaverOAuthClient;
import com.artistry.artistry.auth.oauth.OAuthMember;
import com.artistry.artistry.auth.properties.NaverUserResponse;
import com.artistry.artistry.auth.properties.TokenResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NaverOAuthClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NaverOAuthClient naverOAuthClient;

    private TokenResponse tokenResponse;
    private NaverUserResponse naverUserResponse;

    final static String NAVER_PROFILE_URL = "https://openapi.naver.com/v1/nid/me";



    @BeforeEach
    void setUp() {
        naverOAuthClient = new NaverOAuthClient(
                "redirectUri",
                "clientId",
                "clientSecret",
                "tokenUri",
                restTemplate,
                objectMapper
        );

        tokenResponse =
                TokenResponse.builder()
                        .access_token("testAccessToken")
                .build();

        naverUserResponse = new NaverUserResponse("testNickname","test@example.com","testProfileImage");
    }

    @DisplayName("네이버 Api에 요청을 보내, OAuthMember를 생성한다.")
    @Test
    void testCreateOAuthMember() throws Exception {


        // Mocking the response from the external API
        String responseJson = "{\"response\":{\"email\":\"test@example.com\",\"nickname\":\"testNickname\",\"profile_image\":\"testProfileImage\"}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(NAVER_PROFILE_URL),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        JsonNode responseNode = new ObjectMapper().readTree(responseJson).path("response");
        when(objectMapper.readTree(responseJson)).thenReturn(responseNode);
        when(objectMapper.treeToValue(responseNode, NaverUserResponse.class)).thenReturn(naverUserResponse);

        OAuthMember oAuthMember = naverOAuthClient.createOAuthMember(tokenResponse);

        assertEquals("test@example.com", oAuthMember.getEmail());
        assertEquals("testNickname", oAuthMember.getNickName());
        assertEquals("testProfileImage", oAuthMember.getProfileImageUrl());
    }

    @DisplayName("response의 HttpStatus가 적절하지 않을 경우 예외를 호출한다.")
    @Test
    public void testGetOauthProfileWithNon2xxResponse() {


        String accessToken = "validAccessToken";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> request = new HttpEntity<>(headers);

        when(restTemplate.exchange(eq(NAVER_PROFILE_URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("error", HttpStatus.BAD_REQUEST));

        assertThrows(ArtistryOAuthException.class, () -> naverOAuthClient.getOauthProfile(accessToken));
    }
}