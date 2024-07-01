package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Repository.Query.TeamQueryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TeamRepository extends JpaRepository<Team,Long>, JpaSpecificationExecutor<Team> {

    Slice<Team> findSliceBy(Pageable pageable);

    @Query("SELECT t "
    + "FROM Team t join t.tags tag "
    + "WHERE tag.id IN :tagIds "
    + "order by t.createdAt desc ")
    List<Team> findByTagIds(@Param("tagIds") Set<Long> tagIds);

    @Query(value = "SELECT * "
            + "FROM team t "
            + "WHERE t.name like %:name% ", nativeQuery = true)
    List<Team> findByNameLike(@Param("name") final String name);

    @Query("SELECT t "
    + "FROM Team t join t.teamRoles tr "
    + "WHERE tr.role.id IN :roles ")
    List<Team> findByRoleIds(@Param("roles") Set<Long> roleIds);


}
