package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TagService tagService;
    private final RoleService roleService;
    private final MemberService memberService;
    private final ApplicationService applicationService;

    public TeamResponse findById(Long id){
        return TeamResponse.from(teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new));
    }

    public List<TeamResponse> findTeamsByNameLike(final String name){
        return findByNameLike(name).stream()
                .map(TeamResponse::from)
                .collect(Collectors.toList());
    }

    public List<TeamResponse> findTeamsByApprovedMember(final Long memberId){
        List<ApplicationResponse> responses = applicationService.findByIdAndStatus(memberId, ApplicationStatus.APPROVED);
        List<Long> teamIds = responses.stream().map(ApplicationResponse::getTeamId).collect(Collectors.toList());
        return findTeamsByIds(teamIds, teamRepository::findAllById);
    }

    public List<TeamResponse> findTeamsByTagIds(final List<Long> tagIds) {
        return findTeamsByIds(tagIds, teamRepository::findByTagIds);
    }

    public List<TeamResponse> findTeamsByRoleIds(final List<Long> roleIds) {
        return findTeamsByIds(roleIds, teamRepository::findByRoleIds);
    }



    private <T> List<TeamResponse> findTeamsByIds(final List<Long > ids, Function<Set<Long>, List<Team>> findByIdsFunction){
        Set<Long> distinctIds = new HashSet<>(ids);
        return findByIdsFunction.apply(distinctIds).stream()
                .map(TeamResponse::from)
                .collect(Collectors.toList());
    }

    private List<Team> findByNameLike(final String name){
        return teamRepository.findByNameLike(name);
    }

    public TeamResponse create(TeamRequest teamRequest){
        Member host = memberService.findEntityById(teamRequest.getHostId());
        List<Tag> tags = tagService.findAllEntityById(teamRequest.getTags());
        List<Role> roles = roleService.findAllById(teamRequest.getRoles());

        Team team = new Team(teamRequest.getTeamName(),host,tags,roles);

        return TeamResponse.from(teamRepository.save(team));
    }

}
