package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.*;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TagService tagService;
    private final RoleService roleService;
    private final MemberService memberService;

    public TeamService(TeamRepository teamRepository,TagService tagService,
                       RoleService roleService, MemberService memberService){
        this.teamRepository = teamRepository;
        this.tagService = tagService;
        this.roleService = roleService;
        this.memberService = memberService;
    }

    public TeamResponse findById(Long id){
        return TeamResponse.from(teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new));
    }

    public TeamResponse create(TeamRequest teamRequest){
        Member host = memberService.findById(teamRequest.getHostId());
        List<Tag> tags = tagService.findAllById(teamRequest.getTags());
        List<Role> roles = roleService.findAllById(teamRequest.getRoles());

        Team team = new Team(teamRequest.getTeamName(),host,tags,roles);

        return TeamResponse.from(teamRepository.save(team));
    }

    public List<PortfolioResponse> getApprovedPortfolios(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        return team.getTeamRoles().stream()
                .flatMap(teamRole -> teamRole.getPortfolios(ApplicationStatus.APPROVED)
                        .stream()
                        .map(PortfolioResponse::from))
                .collect(Collectors.toList());
    }
}
