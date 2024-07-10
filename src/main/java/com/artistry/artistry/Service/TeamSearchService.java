package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.PortfolioRequest;
import com.artistry.artistry.Dto.Request.TeamCreateRequest;
import com.artistry.artistry.Dto.Request.TeamSearchRequest;
import com.artistry.artistry.Dto.Request.TeamUpdateRequest;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.ApplicationRepository;
import com.artistry.artistry.Repository.Query.TeamQueryRepositoryCustom;
import com.artistry.artistry.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamSearchService {
    private final TeamRepository teamRepository;
    private final TeamQueryRepositoryCustom teamQueryRepositoryCustom;

    public TeamResponse findById(Long id){
        return TeamResponse.from(findEntityById(id));
    }

    public Team findEntityById(Long id){
        return teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> searchTeams(TeamSearchRequest request, final Pageable pageable) {
        Optional<String> name = Optional.ofNullable(request.getName());
        Optional<List<Long>> roleIds = Optional.ofNullable(request.getRoleIds());
        Optional<List<Long>> tagIds = Optional.ofNullable(request.getTagIds());
        Optional<TeamStatus> status = Optional.ofNullable(request.getTeamStatus());

        Slice<TeamResponse> teams =
                teamQueryRepositoryCustom.searchTeamsWithCriteria(
                        name.orElse(null),
                        roleIds.orElse(null),
                        tagIds.orElse(null),
                        status.orElse(null),
                        pageable);

        return teams.stream().toList();
    }

}
