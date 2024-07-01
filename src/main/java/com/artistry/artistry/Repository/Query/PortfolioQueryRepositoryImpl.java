package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Repository.PortfolioRepository;
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
public class PortfolioQueryRepositoryImpl implements PortfolioQueryRepository {
    @Autowired
    PortfolioRepository portfolioRepository;

    @Override
    public Slice<Portfolio> searchPortfoliosWithCriteria(Optional<String> title, Optional<Long> memberId, Optional<Long> roleId,Optional<PortfolioAccess> access, Pageable pageable) {
        Specification<Portfolio> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add predicates based on the request parameters
            title.ifPresent(n ->
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + n + "%"))
            );

            roleId.ifPresent(r ->
                    predicates.add(criteriaBuilder.equal(root.get("role").get("id"), r))
            );

            memberId.ifPresent(m ->
                    predicates.add(criteriaBuilder.equal(root.get("member").get("id"), m))
            );

            access.ifPresent(st ->
                    predicates.add(criteriaBuilder.equal(root.get("access"), st))
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return portfolioRepository.findAll(spec, pageable);
    }

}
