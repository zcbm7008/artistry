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
    private String title;
    private String roleName;
    private List<ContentResponse> contents;
    private String access;

    public static PortfolioResponse from(Portfolio portfolio){
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .roleName(portfolio.getRole().getName())
                .contents(portfolio.getContents().stream().map(ContentResponse::from).collect(Collectors.toList()))
                .access(portfolio.getPortfolioAccess().toString())
                .build();
    }

}
