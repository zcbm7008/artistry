package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Response.TeamResponse;

import com.artistry.artistry.Service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamQueryRepositoryCustomImpl implements TeamQueryRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(TeamQueryRepositoryCustomImpl.class);

    private final TeamQueryRepository teamQueryRepository;
    private final TeamService teamService;


    public TeamQueryRepositoryCustomImpl(TeamQueryRepository teamQueryRepository, TeamService teamService) {
        this.teamQueryRepository = teamQueryRepository;
        this.teamService = teamService;
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<TeamResponse> searchTeamsWithCriteria(
            String name,
            List<Long> roleIds,
            List<Long> tagIds,
            TeamStatus status,
            Pageable pageable) {
        logger.info("Searching teams with criteria. Name: {}, RoleIds: {}, TagIds: {}, Status: {}", name, roleIds, tagIds, status);
        Slice<Long> teamIdsSlice = teamQueryRepository.searchTeamIdsWithCriteria(name, roleIds, tagIds, status, pageable);

        List<Long> teamIds = teamIdsSlice.getContent();
        logger.info("Found teamids : {}", teamIds);

        List<TeamResponse> responses = teamService.findTeamsByIds(teamIds);

        logger.info("Search completed. Found {} teams.", responses.size());
        return new SliceImpl<>(responses, pageable, teamIdsSlice.hasNext());
    }
}
