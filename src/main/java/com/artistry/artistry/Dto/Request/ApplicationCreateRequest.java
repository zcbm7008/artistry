package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Dto.Response.TeamResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationCreateRequest {
    private TeamResponse team;
    private RoleRequest role;
    private PortfolioRequest portfolio;
    private String status;

}
