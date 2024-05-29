package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.Exceptions.ArtistryOAuthException;
import com.artistry.artistry.auth.properties.NaverUserResponse;
import com.artistry.artistry.auth.properties.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class NaverOAuthClient implements OAuthClient{
    private static final String JWT_DELIMITER = "\\.";

    private final String redirectUri;
    private final String clientId;
    private final String clientSecret;
    private final String tokenUri;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String NAVER_PROFILE_URL = "https://openapi.naver.com/v1/nid/me";


    public NaverOAuthClient(@Value("${oauth.naver.redirect_uri}") final String redirectUri,
                             @Value("${oauth.naver.client_id}") final String clientId,
                             @Value("${oauth.naver.client_secret}") final String clientSecret,
                             @Value("${oauth.naver.token_uri}") final String tokenUri,
                             final RestTemplate restTemplate, final ObjectMapper objectMapper) {
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUri = tokenUri;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public OAuthMember createOAuthMember(final TokenResponse tokenResponse) throws JsonProcessingException {
        NaverUserResponse naverUserResponse = getOauthProfile(tokenResponse.getAccess_token());

        return new OAuthMember(naverUserResponse.getEmail(),naverUserResponse.getNickname(),naverUserResponse.getProfile_image());
    }

    public NaverUserResponse getOauthProfile(String accessToken) throws JsonProcessingException {
        HttpHeaders httpHeaders = createAccessHeaders(accessToken);
        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(NAVER_PROFILE_URL, HttpMethod.GET, request, String.class);
        checkStatusCode(response);

        return parseJsonToResponse(response.getBody());
    }

    private void checkStatusCode(ResponseEntity<String> response){
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ArtistryOAuthException("네이버 API에서 사용자 프로필을 가져오는 데 실패했습니다. 상태 코드: " + response.getStatusCode());
        }
    }

    private HttpHeaders createAccessHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private NaverUserResponse parseJsonToResponse(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json).path("response");;

        return objectMapper.treeToValue(rootNode, NaverUserResponse.class);
    }

    @Override
    public TokenResponse getAccessToken(final String code) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final MultiValueMap<String, String> params = generateRequestParams(code);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);

        final ResponseEntity<TokenResponse> naverTokenResponseEntity = restTemplate.postForEntity("https://nid.naver.com/oauth2.0/token", request, TokenResponse.class);
        return naverTokenResponseEntity.getBody();
    }
    private MultiValueMap<String, String> generateRequestParams(final String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        return params;
    }

}
