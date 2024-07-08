package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Service.TeamService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TeamQueryRepositoryCustomImpl implements TeamQueryRepositoryCustom {
    private final TeamQueryRepository teamQueryRepository;
    private final TeamService teamService;

    public TeamQueryRepositoryCustomImpl(TeamQueryRepository teamQueryRepository, TeamService teamService) {
        this.teamQueryRepository = teamQueryRepository;
        this.teamService = teamService;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<TeamResponse> searchTeamsWithCriteria(
            Optional<String> name,
            Optional<List<Long>> roleIds,
            Optional<List<Long>> tagIds,
            Optional<TeamStatus> status,
            Pageable pageable) {
        Slice<Long> teamIdsSlice = teamQueryRepository.searchTeamIdsWithCriteria(name, roleIds, tagIds, status, pageable);

        List<Long> teamIds = teamIdsSlice.getContent();

        List<TeamResponse> responses = teamService.findTeamsByIds(teamIds);

        return new SliceImpl<>(responses, pageable, teamIdsSlice.hasNext());
    }
}
