package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Response.TeamResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamQueryRepositoryCustom {

    Slice<TeamResponse> searchTeamsWithCriteria(@Param("name") String name,
                                        @Param("roleIds") List<Long> roleIds,
                                        @Param("tagIds") List<Long> tagIds,
                                        @Param("status") TeamStatus status,
                                        Pageable pageable);
}
