package com.artistry.artistry.Service;

import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.oauth.OAuthMemberResponse;
import com.artistry.artistry.auth.oauth.OAuthProviderFactory;
import com.artistry.artistry.auth.oauth.SocialType;
import com.artistry.artistry.auth.oauth.endPoint.OAuthEndPoint;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OAuthService {
    private final MemberService memberService;
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
        TokenResponse tokenResponse = oAuthClient.getAccessToken(code);
        OAuthMemberResponse oAuthMemberResponse = oAuthClient.createOAuthMember(tokenResponse);
        createMemberIfNotExists(oAuthMemberResponse);

        MemberResponse member = memberService.findByEmail(oAuthMemberResponse.getEmail());

        return generateIdToken(member.getId());
    }

    private AccessTokenResponse generateIdToken(Long id) {
        String token = jwtTokenProvider.createIdToken(id);
        return new AccessTokenResponse(token);
    }

    private void createMemberIfNotExists(final OAuthMemberResponse oAuthMemberResponse){
        if(memberService.isEmailExists(oAuthMemberResponse.getEmail())){
            return;
        }
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(oAuthMemberResponse.getNickName(), oAuthMemberResponse.getEmail(),oAuthMemberResponse.getProfileImageUrl());
        memberService.create(memberCreateRequest);
    }

}
