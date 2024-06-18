package com.artistry.artistry.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationCreateRequest {
    private TeamInfoRequest team;
    private RoleRequest role;
    private PortfolioRequest portfolio;
    private String status;
    private String type;

    public ApplicationCreateRequest(TeamInfoRequest team, RoleRequest role, PortfolioRequest portfolio, String status){
        this(team,role,portfolio,status,"APPLICATION");
    }

}
