package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.portfolio.Content;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Dto.Request.ContentRequest;
import com.artistry.artistry.Dto.Request.PortfolioRequest;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Exceptions.PortfolioNotFoundException;
import com.artistry.artistry.Repository.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final RoleService roleService;
    public PortfolioService(PortfolioRepository portfolioRepository,RoleService roleService){
        this.portfolioRepository = portfolioRepository;
        this.roleService = roleService;
    }

    public Portfolio findById(Long id){
        return portfolioRepository.findById(id)
                .orElseThrow(PortfolioNotFoundException::new);
    }

    public List<PortfolioResponse> findAll() {
        List <Portfolio> portfolios = portfolioRepository.findAll();

        return portfolios.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    public List<PortfolioResponse> findAllPublic() {
        List <Portfolio> portfolios = portfolioRepository.findByPortfolioAccess(PortfolioAccess.PUBLIC);

        return portfolios.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PortfolioResponse create(final PortfolioRequest request){
        Role role = roleService.findRoleById(request.getRole().getId());

        Portfolio portfolio = new Portfolio(request.getTitle(),role);
        portfolio.addContents(ContentToEntity(request.getContents()));

        return PortfolioResponse.from(portfolioRepository.save(portfolio));
    }

    public static List<Content> ContentToEntity(List<ContentRequest> contents){
        return contents.stream().map(ContentRequest::toEntity).collect(Collectors.toList());
    }
}
