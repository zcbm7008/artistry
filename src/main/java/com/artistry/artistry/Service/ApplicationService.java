package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.application.ApplicationType;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.ApplicationCreateRequest;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Exceptions.ApplicationNotFoundException;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.ApplicationRepository;
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
    private RoleService roleService;

    @Autowired
    private MemberService memberService;
    @Autowired
    private PortfolioService portfolioService;

    public ApplicationResponse findById(Long id){
        return ApplicationResponse.from(findEntityById(id));
    }

    public Application findEntityById(Long id){
        return applicationRepository.findById(id)
                .orElseThrow(ApplicationNotFoundException::new);
    }

    @Transactional
    public ApplicationResponse changedApplicationStatus(Long id,ApplicationStatus status){
        Application application = findEntityById(id);
        application.setStatus(status);

        return ApplicationResponse.from(application);
    }

    public List<ApplicationResponse> findByIdAndStatus(Long memberId,ApplicationStatus status){
        Member member = memberService.findEntityById(memberId);
        return applicationRepository.findByStatusAndPortfolio_Member(status,member)
                .stream().map(ApplicationResponse::from)
                .collect(Collectors.toList());
    }

    public ApplicationResponse create(final ApplicationCreateRequest request){
        Portfolio portfolio = portfolioService.findEntityById(request.getPortfolio().getId());
        Team team = teamRepository.findById(request.getTeam().getId())
                .orElseThrow(TeamNotFoundException::new);
        Role role = roleService.findEntityById(request.getRole().getId());

        Application application =
                Application.builder()
                        .portfolio(portfolio)
                        .teamRole(team.findTeamRoleByRole(role))
                        .status(ApplicationStatus.of(request.getStatus()))
                        .applicationType(ApplicationType.of(request.getType()))
                        .build();

        applicationRepository.save(application);

        return ApplicationResponse.from(application);
    }

}

