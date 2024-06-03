package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Request.MemberInfoRequest;
import com.artistry.artistry.Dto.Request.MemberUpdateRequest;
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



    public Member findEntityById(Long id){
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public MemberResponse findById(Long id){
        return MemberResponse.from(findEntityById(id));
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

    public MemberResponse create(final MemberCreateRequest request){
        Member member = memberRepository.save(request.toEntity());

        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse update(final Long memberId, final MemberUpdateRequest request){
        Member member = findEntityById(memberId);
        member.update(request.getNickName(),request.getIconUrl());

        return MemberResponse.from(findEntityById(memberId));
    }

    @Transactional
    public void delete(Member member){
        memberRepository.delete(member);
    }

    public MemberResponse findByEmail(String email){
        return MemberResponse.from(memberRepository.findByEmail(email));
    }

    public boolean isEmailExists(String email){
        return memberRepository.existsByEmail(email);
    }


}
