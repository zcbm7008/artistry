package com.artistry.artistry.auth.oauth.Client;

import com.artistry.artistry.auth.oauth.OAuthMember;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OAuthClient {


    OAuthMember createOAuthMember(final TokenResponse tokenResponse) throws JsonProcessingException;
    TokenResponse getAccessToken(final String code);

}
