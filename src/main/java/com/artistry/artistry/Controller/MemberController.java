package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Request.TagCreateRequest;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Service.MemberService;
import com.artistry.artistry.Service.PortfolioService;
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


}
