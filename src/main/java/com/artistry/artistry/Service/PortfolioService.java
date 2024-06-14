package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.portfolio.Content;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Exceptions.PortfolioNotFoundException;
import com.artistry.artistry.Repository.PortfolioRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final RoleService roleService;
    private final MemberService memberService;

    public PortfolioService(PortfolioRepository portfolioRepository,RoleService roleService,MemberService memberService){
        this.portfolioRepository = portfolioRepository;
        this.roleService = roleService;
        this.memberService = memberService;
    }
    @Transactional
    public PortfolioResponse create(final PortfolioCreateRequest request){
        Role role = roleService.findEntityById(request.getRole().getId());
        Member member = memberService.findEntityById(request.getMemberId());

        Portfolio portfolio = new Portfolio(member,request.getTitle(),role);
        portfolio.addContents(ContentToEntity(request.getContents()));
        portfolio.setPortfolioAccess(PortfolioAccess.valueOf(request.getAccess()));

        return PortfolioResponse.from(portfolioRepository.save(portfolio));
    }

    public PortfolioResponse findPortfolioById(Long id){
        return PortfolioResponse.from(findEntityById(id));
    }

    public Portfolio findEntityById(Long id){
        return portfolioRepository.findById(id)
                .orElseThrow(PortfolioNotFoundException::new);
    }

    public List<PortfolioResponse> findAll() {
        List <Portfolio> portfolios = portfolioRepository.findAll();

        return getPortfolioResponses(portfolios);
    }

    @Transactional
    public List<PortfolioResponse> findAllByAccess(PortfolioAccess portfolioAccess) {
        List <Portfolio> portfolios = portfolioRepository.findByPortfolioAccess(portfolioAccess);

        return getPortfolioResponses(portfolios);
    }

    @Transactional
    public List<PortfolioResponse> findAllPublicByTitle(String title){
        List <Portfolio> portfolios = portfolioRepository.findByTitleContainingAndPortfolioAccess(title,PortfolioAccess.PUBLIC);

        return getPortfolioResponses(portfolios);
    }

    @Transactional
    public List<PortfolioResponse> findAllPublicByMember(MemberInfoRequest memberInfoRequest){
        Member member = memberService.findEntityById(memberInfoRequest.getId());
        List <Portfolio> portfolios = portfolioRepository.findByMemberAndPortfolioAccess(member,PortfolioAccess.PUBLIC);

        return getPortfolioResponses(portfolios);
    }

    @Transactional
    public List<PortfolioResponse> findAllPublicByRole(RoleRequest request){
        Role role = roleService.findEntityById(request.getId());
        List <Portfolio> portfolios = portfolioRepository.findByRoleAndPortfolioAccess(role,PortfolioAccess.PUBLIC);

        return getPortfolioResponses(portfolios);
    }

    @Transactional
    public List<PortfolioResponse> searchPublicPortfolios(PortfolioSearchRequest request) {
        Specification<Portfolio> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            String title = request.getTitle();
            Long memberId = request.getMemberId();
            Long roleId = request.getRoleId();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (memberId != null) {
                predicates.add(criteriaBuilder.equal(root.get("member").get("id"), memberId));
            }
            if (roleId != null) {
                predicates.add(criteriaBuilder.equal(root.get("role").get("id"), roleId));
            }
            predicates.add(criteriaBuilder.equal(root.get("portfolioAccess"), PortfolioAccess.PUBLIC));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Portfolio> portfolios = portfolioRepository.findAll(spec);
        return getPortfolioResponses(portfolios);
    }

    @Transactional
    public PortfolioResponse update(final PortfolioUpdateRequest request){
        Portfolio portfolio = findEntityById(request.getId());
        Role role = roleService.findEntityById(request.getRole().getId());
        portfolio.update(request.getTitle(),role,ContentToEntity(request.getContents()),PortfolioAccess.valueOf(request.getAccess()));

        return PortfolioResponse.from(portfolio);
    }

    @Transactional
    public void delete(final Long id){
        Portfolio portfolio = findEntityById(id);
        portfolioRepository.delete(portfolio);
    }

    private static List<PortfolioResponse> getPortfolioResponses(List<Portfolio> portfolios) {
        return portfolios.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    public static List<Content> ContentToEntity(List<ContentRequest> contents){
        return contents.stream()
                .map(ContentRequest::toEntity)
                .collect(Collectors.toList());
    }

}
