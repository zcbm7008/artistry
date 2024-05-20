package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.Portfolio;
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
    private String role;
    private String title;
    private String memberNickname;

    public static PortfolioResponse from(Portfolio portfolio){
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .role(portfolio.getRole().toString())
                .memberNickname(portfolio.getMember().getNickname())
                .build();
    }

}
