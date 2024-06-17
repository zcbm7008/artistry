package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long>, JpaSpecificationExecutor<Portfolio> {

    Slice<Portfolio> findSliceBy(Pageable pageable);

    Slice<Portfolio> findByMemberAndPortfolioAccess(Member member,PortfolioAccess portfolioAccess,Pageable pageable);
    Slice<Portfolio> findByRoleAndPortfolioAccess(Role role,PortfolioAccess portfolioAccess,Pageable pageable);
    Slice<Portfolio> findByTitleContainingAndPortfolioAccess(String title, PortfolioAccess portfolioAccess,Pageable pageable);

    Slice<Portfolio> findByPortfolioAccess(PortfolioAccess portfolioAccess,Pageable pageable);
}
