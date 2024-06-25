package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Request.MemberUpdateRequest;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.TagNameResponse;
import com.artistry.artistry.Service.MemberService;
import com.artistry.artistry.auth.Authorization;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody final MemberCreateRequest request){
        MemberResponse response = memberService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<MemberResponse> findMyProfile(@Authorization Member member){
        MemberResponse response = memberService.findById(member.getId());

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{memberId}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable final Long memberId,
                                                 @Valid @RequestBody final MemberUpdateRequest request){
        MemberResponse response = memberService.update(memberId,request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nickname")
    public ResponseEntity<List<MemberResponse>> findNickname(@RequestParam(defaultValue = "") final String nickname, final Pageable pageable){
        return ResponseEntity.ok(memberService.findAllByNickName(nickname, pageable));
    }

    @DeleteMapping(value = "/{memberId}")
    public ResponseEntity<Void> delete(@PathVariable final long memberId){
        memberService.delete(memberService.findEntityById(memberId));

        return ResponseEntity.noContent().build();
    }



}
