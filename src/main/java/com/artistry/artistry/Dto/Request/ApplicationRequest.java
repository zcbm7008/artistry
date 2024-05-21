package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {
    private Long applicationId;
    private TeamResponse team;
    private long memberId;
    private String role;
    private PortfolioResponse portfolio;
    private String status;
}
