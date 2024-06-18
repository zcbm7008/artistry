package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.application.ApplicationType;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.ApplicationCreateRequest;
import com.artistry.artistry.Dto.Request.ApplicationStatusUpdateRequest;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Exceptions.ApplicationNotFoundException;
import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.ApplicationRepository;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MemberService memberService;
    @Autowired
    private PortfolioService portfolioService;

    @Transactional
    public ApplicationResponse createApplication(final ApplicationCreateRequest request, Long memberId){
        Team team = teamRepository.findById(request.getTeam().getId())
                .orElseThrow(TeamNotFoundException::new);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        validateAuth(ApplicationType.of(request.getType()),team,member);

        Portfolio portfolio = portfolioService.findEntityById(request.getPortfolio().getId());
        Role role = roleService.findEntityById(request.getRole().getId());

        Application application = buildApplication(request, portfolio, team, role);

        return ApplicationResponse.from(applicationRepository.save(application));
    }

    private void validateAuth(ApplicationType applicationType,Team team,Member member){
        if(applicationType.equals(ApplicationType.INVITATION)){
            team.validateHost(member);
        }
    }

    private static Application buildApplication(ApplicationCreateRequest request, Portfolio portfolio, Team team, Role role) {
        Application application =
                Application.builder()
                        .portfolio(portfolio)
                        .teamRole(team.findTeamRoleByRole(role))
                        .status(ApplicationStatus.of(request.getStatus()))
                        .type(ApplicationType.of(request.getType()))
                        .build();
        return application;
    }

    public ApplicationResponse findById(Long id){
        return ApplicationResponse.from(findEntityById(id));
    }

    public Application findEntityById(Long id){
        return applicationRepository.findById(id)
                .orElseThrow(ApplicationNotFoundException::new);
    }

    @Transactional
    public ApplicationResponse changeStatus(ApplicationStatusUpdateRequest request){
        Application application = findEntityById(request.getApplication().getId());

        Member approver = memberRepository.findById(request.getMemberId())
                        .orElseThrow(MemberNotFoundException::new);

        application.changeStatus(ApplicationStatus.of(request.getStatus()),approver);

        return ApplicationResponse.from(application);
    }

    public List<ApplicationResponse> findByIdAndStatus(Long memberId,ApplicationStatus status){
        Member member = memberService.findEntityById(memberId);
        return applicationRepository.findByStatusAndPortfolio_Member(status,member)
                .stream().map(ApplicationResponse::from)
                .collect(Collectors.toList());
    }

}

