package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Service.TagService;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/api/fake")
@RestController
public class FakeController {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TagService tagService;

    public FakeController(MemberRepository memberRepository,JwtTokenProvider jwtTokenProvider, TagService tagService){
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tagService = tagService;
    }

    @GetMapping("/auth/tokens")
    public ResponseEntity<AccessTokenResponse> createFakeToken(@RequestParam String email){
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            member = memberRepository.save(new Member("loginTestMember","email","a.url"));
        }
        Long id = member.getId();

        return ResponseEntity.ok(new AccessTokenResponse(jwtTokenProvider.createIdToken(id)));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        try {
            // 실제 비즈니스 로직
            Thread.sleep(5000); // 5초 지연 추가
            return ResponseEntity.ok(tagService.findAll());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
