package com.artistry.artistry.auth.oauth.Client;

import com.artistry.artistry.auth.oauth.OAuthMemberResponse;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OAuthClient {


    OAuthMemberResponse createOAuthMember(final TokenResponse tokenResponse) throws JsonProcessingException;
    TokenResponse getAccessToken(final String code);

}
