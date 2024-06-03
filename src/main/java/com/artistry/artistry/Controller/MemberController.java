package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Request.MemberUpdateRequest;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody final MemberCreateRequest request){
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{memberId}")
    public ResponseEntity<MemberResponse> updateTag(@PathVariable final Long memberId,
                                                 @Valid @RequestBody final MemberUpdateRequest request){
        MemberResponse response = memberService.updateMember(memberId,request);
        return ResponseEntity.ok(response);
    }



}
