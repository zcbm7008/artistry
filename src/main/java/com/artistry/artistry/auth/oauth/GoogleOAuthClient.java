package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.auth.properties.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class GoogleOAuthClient implements OAuthClient{
    private static final String JWT_DELIMITER = "\\.";
    private static final int PAYLOAD_INDEX = 1;

    private final String googleRedirectUri;
    private final String googleClientId;
    private final String googleClientSecret;
    private final String googleTokenUri;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleOAuthClient(@Value("${oauth.google.redirect_uri}") final String googleRedirectUri,
                             @Value("${oauth.google.client_id}") final String googleClientId,
                             @Value("${oauth.google.client_secret}") final String googleClientSecret,
                             @Value("${oauth.google.token_uri}") final String googleTokenUri,
                             final RestTemplate restTemplate, final ObjectMapper objectMapper) {
        this.googleRedirectUri = googleRedirectUri;
        this.googleClientId = googleClientId;
        this.googleClientSecret = googleClientSecret;
        this.googleTokenUri = googleTokenUri;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public OAuthMember createOAuthMember(final TokenResponse tokenResponse){
        final String idToken = tokenResponse.getId_token();
        final String email = extractElementFromToken(idToken,"email");
        final String name = extractElementFromToken(idToken,"name");
        final String picture = extractElementFromToken(idToken, "picture");

        return new OAuthMember(email,name,picture);
    }

    private String extractElementFromToken(final String idToken, final String key) {
        final String payLoad = idToken.split("\\.")[PAYLOAD_INDEX];
        final String decodedPayLoad = new String(Base64.getDecoder().decode(payLoad));
        final JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        return (String) jacksonJsonParser.parseMap(decodedPayLoad)
                .get(key);
    }

    private TokenResponse requestGoogleToken(final String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = generateRequestParams(code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return restTemplate.postForEntity(googleTokenUri, request, TokenResponse.class).getBody();
    }

    @Override
    public TokenResponse getAccessToken(final String code) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final MultiValueMap<String, String> params = generateRequestParams(code);
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);
        final ResponseEntity<TokenResponse> googleTokenResponseResponseEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, TokenResponse.class);
        return googleTokenResponseResponseEntity.getBody();

    }
    private MultiValueMap<String, String> generateRequestParams(final String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");
        return params;
    }



}
