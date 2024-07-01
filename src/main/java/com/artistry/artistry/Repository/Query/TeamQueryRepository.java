package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;


public interface TeamQueryRepository {
    Slice<Team> searchTeamsWithCriteria(Optional<String> name, Optional<List<Long>> roleIds, Optional<List<Long>> tagIds, Optional<TeamStatus> status, Pageable pageable);
}
