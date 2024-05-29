package com.artistry.artistry.auth.oauth;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);
}
