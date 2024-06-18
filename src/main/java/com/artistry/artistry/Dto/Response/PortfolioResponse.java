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
    private MemberResponse member;
    private String roleName;
    private List<LinkResponse> contents;
    private String access;

    private Long view;
    private Long like;



    public static PortfolioResponse from(Portfolio portfolio){
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .member(MemberResponse.from(portfolio.getMember()))
                .roleName(portfolio.getRole().getName())
                .contents(portfolio.getContents().stream().map(LinkResponse::from).collect(Collectors.toList()))
                .access(portfolio.getAccess().toString())
                .view(portfolio.getView())
                .like(portfolio.getLike())
                .build();
    }

}
