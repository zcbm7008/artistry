package com.artistry.artistry.Repository.Team;

import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamRole;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Repository.TeamRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public class TeamQueryRepositoryImpl implements TeamQueryRepository {
    @Autowired
    TeamRepository teamRepository;

    @Override
    public Slice<Team> searchTeamsWithCriteria(Optional<String> name, Optional<List<Long>> roleIds, Optional<List<Long>> tagIds, Optional<TeamStatus> status, Pageable pageable){
        Specification<Team> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add predicates based on the request parameters
            name.ifPresent(n ->
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + n + "%"))
            );

            roleIds.filter(ids -> !ids.isEmpty())
                    .ifPresent(ids ->{
                        Join<Team, TeamRole> roles = root.join("teamRoles");
                        predicates.add(roles.get("id").in(ids));
                    });

            tagIds.filter(ids -> !ids.isEmpty())
                    .ifPresent(ids -> {
                        Join<Team, Tag> tags = root.join("tags");
                        predicates.add(tags.get("id").in(ids));
                    });

            status.ifPresent(st ->
                    predicates.add(criteriaBuilder.equal(root.get("teamStatus"), st))
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return teamRepository.findAll(spec, pageable);

    }

}
