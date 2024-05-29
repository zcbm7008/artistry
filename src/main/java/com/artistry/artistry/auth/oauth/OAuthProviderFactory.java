package com.artistry.artistry.auth.oauth;

public interface OAuthProviderFactory {
    OAuthEndPoint createOAuthEndPoint(SocialType socialType);
    OAuthClient createOAuthClient(SocialType socialType);


}
