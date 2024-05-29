package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.auth.properties.TokenResponse;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);
    TokenResponse getAccessToken(final String code);
}
