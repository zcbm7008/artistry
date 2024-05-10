package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
}
