package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.auth.properties.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OAuthClient {

    OAuthMember createOAuthMember(final TokenResponse tokenResponse) throws JsonProcessingException;

    TokenResponse getAccessToken(final String code);

}
