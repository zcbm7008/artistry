package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.ApplicationRepository;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberService memberService;

    public TeamResponse findById(Long id){
        return TeamResponse.from(teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new));
    }

    public List<ApplicationResponse> findByIdAndStatus(Long memberId,ApplicationStatus status){
        Member member = memberService.findEntityById(memberId);
        return applicationRepository.findByStatusAndMember(status,member)
                .stream().map(ApplicationResponse::from)
                .collect(Collectors.toList());
    }

}

