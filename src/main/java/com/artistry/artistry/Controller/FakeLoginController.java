package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("test")
@RestController
@RequiredArgsConstructor
public class FakeLoginController {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/auth/fake/tokens")
    public ResponseEntity<AccessTokenResponse> createFakeToken(@RequestParam String email){
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            member = memberRepository.save(new Member("loginTestMember","email","a.url"));
        }


        Long id = member.getId();
        System.out.println(id);
        return ResponseEntity.ok(new AccessTokenResponse(jwtTokenProvider.createIdToken(id)));
    }
}
