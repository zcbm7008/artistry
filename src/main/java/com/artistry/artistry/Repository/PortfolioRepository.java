package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long>, JpaSpecificationExecutor<Portfolio> {
    List<Portfolio> findByMemberAndPortfolioAccess(Member member,PortfolioAccess portfolioAccess);
    List<Portfolio> findByRoleAndPortfolioAccess(Role role,PortfolioAccess portfolioAccess);
    List<Portfolio> findByTitleContainingAndPortfolioAccess(String title, PortfolioAccess portfolioAccess);

    List<Portfolio> findByPortfolioAccess(PortfolioAccess portfolioAccess);
}
