package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.portfolio.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioResponse {
    private Long id;
    private String roleName;
    private String title;

    public static PortfolioResponse from(Portfolio portfolio){
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .roleName(portfolio.getRole().getRoleName())
                .build();
    }

}
