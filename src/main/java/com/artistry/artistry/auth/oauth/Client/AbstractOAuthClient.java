package com.artistry.artistry.auth.oauth.Client;

import com.artistry.artistry.auth.properties.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public abstract class AbstractOAuthClient implements OAuthClient {
    protected final String redirectUri;
    protected final String clientId;
    protected final String clientSecret;
    protected final String tokenUri;
    protected final RestTemplate restTemplate;
    protected final ObjectMapper objectMapper;

    @Override
    public TokenResponse getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = generateRequestParams(code);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(tokenUri, request, TokenResponse.class);
        return responseEntity.getBody();
    }

    protected MultiValueMap<String, String> generateRequestParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        return params;
    }
}