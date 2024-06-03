package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Request.MemberInfoRequest;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.MemberTeamsResponse;
import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Repository.ApplicationRepository;
import com.artistry.artistry.Repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;

    public MemberService(MemberRepository memberRepository,ApplicationRepository applicationRepository){
        this.memberRepository = memberRepository;
        this.applicationRepository = applicationRepository;
    }



    public Member findById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public MemberTeamsResponse getParticipatedTeams(final MemberInfoRequest request){
        final Member member = memberRepository.findByEmail(request.getEmail());
        final List<Application> applications = applicationRepository.findByStatusAndMember(ApplicationStatus.APPROVED, member);

        final List<Team> teams = applications.stream()
                .map(Application::getTeam)
                .toList();

        return MemberTeamsResponse.from(teams);

    }

    public MemberResponse createMember(final MemberCreateRequest request){
        Member member = memberRepository.save(request.toEntity());

        return MemberResponse.from(member);
    }

    public MemberResponse findByEmail(String email){
        return MemberResponse.from(memberRepository.findByEmail(email));
    }

}
