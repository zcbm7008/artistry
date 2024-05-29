package com.artistry.artistry.auth.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NaverOAuthEndPoint implements OAuthEndPoint{
    private static final String NAVER_OAUTH_END_POINT = "https://nid.naver.com/oauth2.0/authorize";
    private static final String STATE = "sadgsdag";

    private final String redirectUri;
    private final String clientId;

    public NaverOAuthEndPoint(@Value("${oauth.naver.redirect_uri}") final String redirectUri,
                              @Value("${oauth.naver.client_id}") final String clientId) {
        this.redirectUri = redirectUri;
        this.clientId = clientId;
    }

    @Override
    public String generate() {
        return NAVER_OAUTH_END_POINT + "?"
                + "client_id=" + clientId + "&"
                + "redirect_uri=" + redirectUri + "&"
                + "response_type=" + "code" + "&"
                + "state=" + STATE;
    }
}
