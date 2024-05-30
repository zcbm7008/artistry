package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.oauth.endPoint.OAuthEndPoint;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OAuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthProviderFactory oAuthProviderFactory;

    public SocialType getSocialType(String provider) {
        try {
            return SocialType.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("정의되지 않은 소셜 타입입니다.", e);
        }
    }

    public String generateLoginLink(SocialType socialType) {
        OAuthEndPoint endpoint = oAuthProviderFactory.createOAuthEndPoint(socialType);
        return endpoint.generate();
    }

    public AccessTokenResponse createMemberAccessToken(SocialType socialType, final String code) throws JsonProcessingException {
        OAuthClient oAuthClient = oAuthProviderFactory.createOAuthClient(socialType);
        TokenResponse tokenResponse = createTokenResponse(oAuthClient,code);
        OAuthMember oAuthMember = oAuthClient.createOAuthMember(tokenResponse);

        createMemberIfNotExists(oAuthMember);

        return generateAccessToken(oAuthMember);
    }

    private AccessTokenResponse generateAccessToken(OAuthMember oAuthMember) {
        String token = jwtTokenProvider.generateEmailToken(oAuthMember.getEmail());
        return new AccessTokenResponse(token);
    }

    private TokenResponse createTokenResponse(OAuthClient oAuthClient, final String code){
        return oAuthClient.getAccessToken(code);
    }

    private void createMemberIfNotExists(final OAuthMember oAuthMember){
        if(memberRepository.findByEmail(oAuthMember.getEmail()) != null){
            return;
        }
        final Member member = new Member(oAuthMember.getNickName(), oAuthMember.getEmail());
        memberRepository.save(member);
    }

}
