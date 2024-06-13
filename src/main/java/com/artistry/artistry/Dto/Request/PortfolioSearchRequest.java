package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioSearchRequest {
    private String title;
    private Long memberId;
    private Long roleId;
    private PortfolioAccess access;
}
