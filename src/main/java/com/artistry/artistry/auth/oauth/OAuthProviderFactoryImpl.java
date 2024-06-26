package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.auth.oauth.Client.GoogleOAuthClient;
import com.artistry.artistry.auth.oauth.Client.NaverOAuthClient;
import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.oauth.endPoint.GoogleOAuthEndPoint;
import com.artistry.artistry.auth.oauth.endPoint.NaverOAuthEndPoint;
import com.artistry.artistry.auth.oauth.endPoint.OAuthEndPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthProviderFactoryImpl implements OAuthProviderFactory{

    private final NaverOAuthEndPoint naverOAuthEndPoint;
    private final NaverOAuthClient naverOAuthClient;
    private final GoogleOAuthEndPoint googleOAuthEndPoint;
    private final GoogleOAuthClient googleOAuthClient;

    @Override
    public OAuthEndPoint createOAuthEndPoint(SocialType socialType) {
        return switch (socialType) {
            case SocialType.GOOGLE -> googleOAuthEndPoint;
            case SocialType.NAVER -> naverOAuthEndPoint;
            default -> throw new IllegalArgumentException("지원되지 않는 플랫폼입니다.");
        };

    }

    @Override
    public OAuthClient createOAuthClient(SocialType socialType) {
        return switch (socialType) {
            case SocialType.GOOGLE -> googleOAuthClient;
            case SocialType.NAVER -> naverOAuthClient;
            default -> throw new IllegalArgumentException("지원되지 않는 플랫폼입니다.");
        };
    }
}
