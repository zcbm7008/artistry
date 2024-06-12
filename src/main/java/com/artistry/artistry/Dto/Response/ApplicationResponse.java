package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.application.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private Long id;
    private MemberResponse member;
    private Long teamId;
    private String role;
    private PortfolioResponse portfolio;
    private String status;

    public static ApplicationResponse from(Application application){
        return ApplicationResponse.builder()
                .id(application.getId())
                .member(MemberResponse.from(application.getMember()))
                .teamId(application.getTeamRole().getTeam().getId())
                .role(application.getRole().getName())
                .portfolio(PortfolioResponse.from(application.getPortfolio()))
                .status(application.getStatus().toString())
                .build();
    }

}
