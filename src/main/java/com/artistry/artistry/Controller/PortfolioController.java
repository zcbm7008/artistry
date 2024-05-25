package com.artistry.artistry.Controller;

import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Dto.Request.PortfolioDeleteRequest;
import com.artistry.artistry.Dto.Request.PortfolioRequest;
import com.artistry.artistry.Dto.Request.PortfolioUpdateRequest;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/portfolios")
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;

    public PortfolioController(final PortfolioService portfolioService){
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> findAllPortfolios(){
        return ResponseEntity.ok(portfolioService.findAll());
    }

    @GetMapping("/access")
    public ResponseEntity<List<PortfolioResponse>> findAllPortfoliosByAccess(@RequestParam(defaultValue = "PUBLIC") String access){
        return ResponseEntity.ok(portfolioService.findAllByAccess(PortfolioAccess.valueOf(access)));
    }
    @PostMapping
    public ResponseEntity<PortfolioResponse> createPortfolio(@Valid @RequestBody final PortfolioRequest request){
        PortfolioResponse response = portfolioService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioResponse> readPortfolio(@PathVariable final Long portfolioId){
        PortfolioResponse response  = portfolioService.findPortfolioById(portfolioId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<PortfolioResponse> update(@RequestBody final PortfolioUpdateRequest request){
        PortfolioResponse response = portfolioService.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{portfolioId}")
    public ResponseEntity<Void> delete(@PathVariable final Long portfolioId){
        portfolioService.delete(portfolioId);
        return ResponseEntity.noContent().build();
    }

}
