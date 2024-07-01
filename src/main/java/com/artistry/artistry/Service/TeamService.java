package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.ApplicationRepository;
import com.artistry.artistry.Repository.Team.TeamQueryRepository;
import com.artistry.artistry.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final ApplicationRepository applicationRepository;
    private final PortfolioService portfolioService;
    private final TeamQueryRepository teamQueryRepository;

    public TeamResponse create(TeamCreateRequest teamCreateRequest){
        Member host = memberService.findEntityById(teamCreateRequest.getHostId());
        List<Tag> tags = tagService.findAllEntityById(teamCreateRequest.getTags());
        List<Role> roles = roleService.findAllById(teamCreateRequest.getRoles());

        Team team = new Team(teamCreateRequest.getName(),host,tags,roles);

        return TeamResponse.from(teamRepository.save(team));
    }

    public TeamResponse findById(Long id){
        return TeamResponse.from(findEntityById(id));
    }

    public Team findEntityById(Long id){
        return teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);
    }

    public List<TeamResponse> findTeamsByNameLike(final String name){
        return findByNameLike(name).stream()
                .map(TeamResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApplicationResponse apply(Long teamId, PortfolioRequest request){
        Team team = findEntityById(teamId);
        Portfolio portfolio = portfolioService.findEntityById(request.getId());
        Application application = team.apply(portfolio);
        applicationRepository.save(application);
        return ApplicationResponse.from(application);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> searchTeams(TeamSearchRequest request, final Pageable pageable) {
        Optional<String> name = Optional.ofNullable(request.getName());
        Optional<List<Long>> roleIds = Optional.ofNullable(request.getRoleIds());
        Optional<List<Long>> tagIds = Optional.ofNullable(request.getTagIds());
        Optional<TeamStatus> status = Optional.ofNullable(request.getTeamStatus());

        Slice<Team> teams = teamQueryRepository.searchTeamsWithCriteria(name, roleIds, tagIds, status, pageable);
        return getTeamResponses(teams);
    }

    private List<Team> findByNameLike(final String name){
        return teamRepository.findByNameLike(name);
    }

    public List<TeamResponse> findTeamsByApprovedMember(final Long memberId){
        List<ApplicationResponse> responses = applicationService.findByIdAndStatus(memberId, ApplicationStatus.APPROVED);
        List<Long> teamIds =
                responses.stream()
                .map(ApplicationResponse::getTeamId)
                .collect(Collectors.toList());

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

    public TeamResponse update(final Long teamId, final TeamUpdateRequest request){
        Team team = findEntityById(teamId);
        List <Tag> tags = tagService.findAllEntityById(request.getTags());
        List <Role> roles = roleService.findAllById(request.getRoles());

        team.update(request.getName(),tags,roles,TeamStatus.of(request.getTeamStatus()));

        return TeamResponse.from(team);
    }

    @Transactional
    public void cancel(final Long teamId){
        Team team = findEntityById(teamId);
        team.cancel();

        teamRepository.delete(team);
    }

    @Transactional
    public TeamResponse finish(final Long teamId){
        Team team = findEntityById(teamId);
        team.finish();

        return TeamResponse.from(team);
    }

    private static List<TeamResponse> getTeamResponses(Slice<Team> teams) {
        return teams.stream()
                .map(TeamResponse::from)
                .collect(Collectors.toList());
    }

}
