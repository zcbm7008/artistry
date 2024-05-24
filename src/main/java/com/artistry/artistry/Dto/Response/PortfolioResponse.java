package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.portfolio.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioResponse {
    private Long id;
    private String roleName;
    private String title;
    private List<ContentResponse> contents;

    public static PortfolioResponse from(Portfolio portfolio){
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .roleName(portfolio.getRole().getRoleName())
                .contents(portfolio.getContents().stream().map(ContentResponse::from).collect(Collectors.toList()))
                .build();
    }

}
