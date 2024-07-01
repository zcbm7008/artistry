package com.artistry.artistry.Repository.Query;

import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;


public interface PortfolioQueryRepository {
    Slice<Portfolio> searchPortfoliosWithCriteria(Optional<String> title, Optional<Long> memberId, Optional<Long> roleId, Optional<PortfolioAccess> access, Pageable pageable);
}
