package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.auth.oauth.Client.OAuthClient;

public interface OAuthProviderFactory {
    OAuthEndPoint createOAuthEndPoint(SocialType socialType);
    OAuthClient createOAuthClient(SocialType socialType);


}
