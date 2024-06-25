package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Dto.Request.ApplicationCreateRequest;
import com.artistry.artistry.Dto.Request.ApplicationStatusUpdateRequest;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Service.ApplicationService;
import com.artistry.artistry.auth.Authorization;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(@Authorization Member member,
                                                      @RequestBody @Valid ApplicationCreateRequest request){
        return ResponseEntity.ok(applicationService.createApplication(member.getId(), request));
    }

    @PatchMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> update(@PathVariable final Long applicationId,
                                                      @Authorization Member member,
                                                      @RequestBody ApplicationStatusUpdateRequest request){
        return ResponseEntity.ok(applicationService.updateStatus(applicationId,member.getId(),request));
    }

}
