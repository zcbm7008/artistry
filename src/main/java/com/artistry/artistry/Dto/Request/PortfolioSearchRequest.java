package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioSearchRequest {
    private String title;
    private Long memberId;
    private Long roleId;
    private PortfolioAccess access;

    public PortfolioSearchRequest(String title, Long memberId, Long roleId) {
        this.title = title;
        this.memberId = memberId;
        this.roleId = roleId;
        this.access = PortfolioAccess.PUBLIC;
    }
}
