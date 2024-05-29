package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.artistry.artistry.auth.oauth.Client.OAuthClient;
import com.artistry.artistry.auth.properties.AccessTokenResponse;
import com.artistry.artistry.auth.properties.TokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OAuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthProviderFactory oAuthProviderFactory;

    public String generateLoginLink(SocialType socialType) {
        OAuthEndPoint endpoint = oAuthProviderFactory.createOAuthEndPoint(socialType);
        return endpoint.generate();
    }

    public AccessTokenResponse createToken(SocialType socialType, final String code) throws JsonProcessingException {
        OAuthClient oAuthClient = oAuthProviderFactory.createOAuthClient(socialType);
        final TokenResponse tokenResponse = oAuthClient.getAccessToken(code);
        final OAuthMember oAuthMember = oAuthClient.createOAuthMember(tokenResponse);
        System.out.println(oAuthMember.getDisplayName() + oAuthMember.getEmail());
        createMemberIfNotExists(oAuthMember);
        return new AccessTokenResponse(jwtTokenProvider.generateEmailToken(oAuthMember.getEmail()));
    }



    private void createMemberIfNotExists(final OAuthMember oAuthMember){
        if(memberRepository.findByEmail(oAuthMember.getEmail()) != null){
            return;
        }
        final Member member = new Member(oAuthMember.getDisplayName(), oAuthMember.getEmail());
        memberRepository.save(member);
    }

}
