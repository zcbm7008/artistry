package com.artistry.artistry.auth.oauth.Client;

import com.artistry.artistry.auth.oauth.OAuthMember;
import com.artistry.artistry.auth.properties.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public interface OAuthClient {


    OAuthMember createOAuthMember(final TokenResponse tokenResponse) throws JsonProcessingException;
    TokenResponse getAccessToken(final String code);

}
