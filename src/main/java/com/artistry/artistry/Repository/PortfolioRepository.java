package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

    List<Portfolio> findByPortfolioAccess(PortfolioAccess portfolioAccess);
}
