//package com.artistry.artistry.Repository.Query;
//
//import com.artistry.artistry.Domain.Role.Role;
//import com.artistry.artistry.Domain.tag.Tag;
//import com.artistry.artistry.Domain.team.Team;
//import com.artistry.artistry.Domain.team.TeamRole;
//import com.artistry.artistry.Domain.team.TeamStatus;
//import com.artistry.artistry.Domain.team.TeamTag;
//import com.artistry.artistry.Repository.TeamRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.TypedQuery;
//import jakarta.persistence.criteria.Join;
//import jakarta.persistence.criteria.JoinType;
//import jakarta.persistence.criteria.Predicate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//@Repository
//public class TeamQueryRepositoryImpl implements TeamQueryRepository {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public Slice<Team> searchTeamsWithCriteria(Optional<String> name, Optional<List<Long>> roleIds, Optional<List<Long>> tagIds, Optional<TeamStatus> status, Pageable pageable) {
//        StringBuilder jpql = new StringBuilder("SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.teamRoles tr LEFT JOIN FETCH t.tags tg WHERE t.deleted = false ");
//
//        if (name.isPresent()) {
//            jpql.append("AND t.name LIKE :name ");
//        }
//        if (roleIds.isPresent() && !roleIds.get().isEmpty()) {
//            jpql.append("AND tr.role.id IN :roleIds ");
//        }
//        if (tagIds.isPresent() && !tagIds.get().isEmpty()) {
//            jpql.append("AND tg.id IN :tagIds ");
//        }
//        if (status.isPresent()) {
//            jpql.append("AND t.teamStatus = :status ");
//        }
//
//        TypedQuery<Team> query = entityManager.createQuery(jpql.toString(), Team.class);
//
//        name.ifPresent(n -> query.setParameter("name", "%" + n + "%"));
//        roleIds.ifPresent(ids -> query.setParameter("roleIds", ids));
//        tagIds.ifPresent(ids -> query.setParameter("tagIds", ids));
//        status.ifPresent(st -> query.setParameter("status", st));
//
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//
//        List<Team> teams = query.getResultList();
//
//        // Total count query for pagination
//        StringBuilder countJpql = new StringBuilder("SELECT COUNT(DISTINCT t) FROM Team t LEFT JOIN t.teamRoles tr LEFT JOIN t.tags tg WHERE t.deleted = false ");
//
//        if (name.isPresent()) {
//            countJpql.append("AND t.name LIKE :name ");
//        }
//        if (roleIds.isPresent() && !roleIds.get().isEmpty()) {
//            countJpql.append("AND tr.role.id IN :roleIds ");
//        }
//        if (tagIds.isPresent() && !tagIds.get().isEmpty()) {
//            countJpql.append("AND tg.id IN :tagIds ");
//        }
//        if (status.isPresent()) {
//            countJpql.append("AND t.teamStatus = :status ");
//        }
//
//        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);
//
//        name.ifPresent(n -> countQuery.setParameter("name", "%" + n + "%"));
//        roleIds.ifPresent(ids -> countQuery.setParameter("roleIds", ids));
//        tagIds.ifPresent(ids -> countQuery.setParameter("tagIds", ids));
//        status.ifPresent(st -> countQuery.setParameter("status", st));
//
//        long total = countQuery.getSingleResult();
//
//        return new PageImpl<>(teams, pageable, total);
//    }
//}
