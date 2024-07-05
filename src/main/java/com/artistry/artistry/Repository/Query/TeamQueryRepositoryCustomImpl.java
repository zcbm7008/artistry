package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TeamQueryRepositoryCustomImpl implements TeamQueryRepositoryCustom {
    @Autowired
    private TeamQueryRepository teamQueryRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService; // TeamService를 주입받음

    @Override
    public Slice<TeamResponse> searchTeamsWithCriteria(
            Optional<String> name,
            Optional<List<Long>> roleIds,
            Optional<List<Long>> tagIds,
            Optional<TeamStatus> status,
            Pageable pageable) {
        Slice<Long> teamIdsSlice = teamQueryRepository.searchTeamIdsWithCriteria(name, roleIds, tagIds, status, pageable);

        List<Long> teamIds = teamIdsSlice.getContent();

        List<TeamResponse> responses = teamService.findTeamsByIds(teamIds); // TeamService를 사용하여 팀 목록을 가져옴

        return new SliceImpl<>(responses, pageable, teamIdsSlice.hasNext());
    }
}
