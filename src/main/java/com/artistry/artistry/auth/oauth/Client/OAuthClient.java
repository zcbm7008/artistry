package com.artistry.artistry.auth.oauth.Client;

import com.artistry.artistry.Dto.Request.OAuthMemberRequest;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.artistry.artistry.auth.oauth.OAuthMemberResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OAuthClient {

    OAuthMemberResponse createOAuthMember(final TokenResponse tokenResponse) throws JsonProcessingException;

    OAuthMemberRequest createOAuthMemberRequest(final TokenResponse tokenResponse) throws JsonProcessingException;
    TokenResponse getAccessToken(final String code);

}
