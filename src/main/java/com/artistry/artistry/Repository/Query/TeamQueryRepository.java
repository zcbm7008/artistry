package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Response.TeamResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamQueryRepository extends JpaRepository<Team,Long>, JpaSpecificationExecutor<Team> {

    @Query("SELECT DISTINCT t.id FROM Team t " +
            "LEFT JOIN t.teamRoles tr " +
            "LEFT JOIN t.tags tg " +
            "WHERE (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:status IS NULL OR t.teamStatus = :status) " +
            "AND (:roleIds IS NULL OR tr.id IN :roleIds) " +
            "AND (:tagIds IS NULL OR tg.id IN :tagIds)")
    Slice<Long> searchTeamIdsWithCriteria(@Param("name") String name,
                              @Param("roleIds") List<Long> roleIds,
                              @Param("tagIds") List<Long> tagIds,
                              @Param("status") TeamStatus status,
                              Pageable pageable);

}
