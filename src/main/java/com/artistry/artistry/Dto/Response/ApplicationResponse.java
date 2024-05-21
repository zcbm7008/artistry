package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {
    private Long applicationId;
    private Long teamId;
    private MemberResponse member;
    private String role;
    private PortfolioResponse portfolio;
    private String status;

    public static ApplicationResponse from(Application application){
        return ApplicationResponse.builder()
                .applicationId(application.getId())
                .teamId(application.getTeam().getId())
                .member(MemberResponse.from(application.getMember()))
                .role(application.getRole().toString())
                .portfolio(PortfolioResponse.from(application.getPortfolio()))
                .status(application.getStatus().toString())
                .build();
    }

}
