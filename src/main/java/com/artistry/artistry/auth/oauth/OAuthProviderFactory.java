package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.oauth.endPoint.OAuthEndPoint;

public interface OAuthProviderFactory {
    OAuthEndPoint createOAuthEndPoint(SocialType socialType);
    OAuthClient createOAuthClient(SocialType socialType);


}
