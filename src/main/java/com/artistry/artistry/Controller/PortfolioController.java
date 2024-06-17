package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Dto.Request.PortfolioCreateRequest;
import com.artistry.artistry.Dto.Request.PortfolioSearchRequest;
import com.artistry.artistry.Dto.Request.PortfolioUpdateRequest;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RequestMapping("/api/portfolios")
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;

    public PortfolioController(final PortfolioService portfolioService){
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> findAllPortfolios(final Pageable pageable){
        return ResponseEntity.ok(portfolioService.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PortfolioResponse>> findPortfoliosByCriteria(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long roleId,
            @RequestParam(defaultValue = "PUBLIC") String access
            , final Pageable pageable) {

        PortfolioSearchRequest request = new PortfolioSearchRequest(title,memberId,roleId);
        return ResponseEntity.ok(portfolioService.searchPublicPortfolios(request,pageable));
    }

    @GetMapping("/access")
    public ResponseEntity<List<PortfolioResponse>> findAllPortfoliosByAccess(@RequestParam(defaultValue = "PUBLIC") String access, final Pageable pageable){
        return ResponseEntity.ok(portfolioService.findAllByAccess(PortfolioAccess.valueOf(access),pageable));
    }

    @PostMapping
    public ResponseEntity<PortfolioResponse> createPortfolio(@Valid @RequestBody final PortfolioCreateRequest request){
        PortfolioResponse response = portfolioService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioResponse> readPortfolio(@PathVariable final Long portfolioId){
        PortfolioResponse response  = portfolioService.findByIdAndIncreaseView(portfolioId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<PortfolioResponse> update(@RequestBody final PortfolioUpdateRequest request){
        PortfolioResponse response = portfolioService.update(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{portfolioId}/like")
    public ResponseEntity<PortfolioResponse> like(@RequestParam final Long portfolioId){
        return ResponseEntity.ok(portfolioService.increaseLike(portfolioId));

    }

    @DeleteMapping(value = "/{portfolioId}")
    public ResponseEntity<Void> delete(@PathVariable final Long portfolioId){
        portfolioService.delete(portfolioId);
        return ResponseEntity.noContent().build();
    }

}
