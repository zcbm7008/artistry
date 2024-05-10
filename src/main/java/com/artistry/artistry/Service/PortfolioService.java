package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Portfolio;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Repository.PortfolioRepository;


public class PortfolioService {
    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository){
        this.portfolioRepository = portfolioRepository;
    }

    public Portfolio findById(Long id){
        return portfolioRepository.findById(id)
                .orElseThrow(RoleNotFoundException::new);
    }
}
