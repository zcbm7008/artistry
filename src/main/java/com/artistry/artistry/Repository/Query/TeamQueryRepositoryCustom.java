package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Response.TeamResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface TeamQueryRepositoryCustom {

    Slice<TeamResponse> searchTeamsWithCriteria(@Param("name") Optional<String> name,
                                        @Param("roleIds") Optional<List<Long>> roleIds,
                                        @Param("tagIds") Optional<List<Long>> tagIds,
                                        @Param("status") Optional<TeamStatus> status,
                                        Pageable pageable);
}
