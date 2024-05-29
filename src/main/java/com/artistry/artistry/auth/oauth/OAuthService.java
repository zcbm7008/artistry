package com.artistry.artistry.auth.oauth;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Service.MemberService;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.artistry.artistry.auth.properties.AccessTokenResponse;
import com.artistry.artistry.auth.properties.TokenResponse;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;


import java.util.Base64;


@Service
public class OAuthService {
    private final OAuthEndPoint oAuthEndpoint;
    private final GoogleOAuthClient oAuthClient;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final int PAYLOAD_INDEX = 1;

    public OAuthService(final OAuthEndPoint oAuthEndpoint,
                        final GoogleOAuthClient oAuthClient,
                       final MemberRepository memberRepository,
                       final MemberService memberService,
                        final JwtTokenProvider jwtTokenProvider) {
        this.oAuthEndpoint = oAuthEndpoint;
        this.oAuthClient = oAuthClient;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String generateLoginLink() {
        return oAuthEndpoint.generate();
    }

    public AccessTokenResponse createToken(final String code) {
        final TokenResponse googleTokenResponse = oAuthClient.getAccessToken(code);
        final OAuthMember oAuthMember = createOAuthMember(googleTokenResponse.getId_token());
        createMemberIfNotExists(oAuthMember);
        return new AccessTokenResponse(jwtTokenProvider.generateEmailToken(oAuthMember.getEmail()));
    }

    private String extractElementFromToken(final String googleIdToken, final String key) {
        final String payLoad = googleIdToken.split("\\.")[PAYLOAD_INDEX];
        final String decodedPayLoad = new String(Base64.getDecoder().decode(payLoad));
        final JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        return (String) jacksonJsonParser.parseMap(decodedPayLoad)
                .get(key);
    }
    private void saveMemberIfNotExists(final OAuthMember oAuthMember, final String email) {
        if (memberService.findByEmail(email) == null) {
            memberRepository.save(generateMemberBy(oAuthMember));
        }
    }
    private void createMemberIfNotExists(final OAuthMember oAuthMember){
        if(memberRepository.findByEmail(oAuthMember.getEmail()) != null){
            return;
        }
        final Member member = new Member(oAuthMember.getDisplayName(), oAuthMember.getEmail());
        memberRepository.save(member);
    }

    public OAuthMember createOAuthMember(final String googleIdToken){
        final String email = extractElementFromToken(googleIdToken,"email");
        final String name = extractElementFromToken(googleIdToken,"name");
        final String picture = extractElementFromToken(googleIdToken, "picture");

        return new OAuthMember(email,name,picture);
    }

    private Member generateMemberBy(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getDisplayName(),oAuthMember.getEmail());
    }

}
