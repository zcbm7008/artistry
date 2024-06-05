package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TeamRoleTest {

    @DisplayName("teamRole의 포트폴리오를 가져온다.")
    @Test
    void getTeamRolePortfolios(){
        String title1 = "title1";
        String title2 = "title2";
        Role role1 = new Role("role1");
        Role role2 = new Role("role2");
        Portfolio portfolio1 = new Portfolio(title1,role1 );
        Portfolio portfolio2 = new Portfolio(title2,role2 );
        Application application1 =
                Application.builder()
                        .role(role1)
                        .status(ApplicationStatus.APPROVED)
                        .portfolio(portfolio1)
                        .build();


        Application application2 =
                Application.builder()
                        .role(role2)
                        .status(ApplicationStatus.REJECTED)
                        .portfolio(portfolio2)
                        .build();

        TeamRole teamRole1 = TeamRole.builder()
                .applications(List.of(application1,application2))
                .build();

    List<Portfolio> approvedPortfolios = teamRole1.getPortfolios(ApplicationStatus.APPROVED);
    List<Portfolio> rejectedPortfolios = teamRole1.getPortfolios(ApplicationStatus.REJECTED);
    List<Portfolio> pendingPortfolios = teamRole1.getPortfolios(ApplicationStatus.PENDING);

    assertThat(approvedPortfolios).hasSize(1).extracting(Portfolio::getTitle).contains(title1);
    assertThat(rejectedPortfolios).hasSize(1).extracting(Portfolio::getTitle).contains(title2);
    assertThat(pendingPortfolios).isNullOrEmpty();
    }

    @DisplayName("TeamRole의 Role을 출력한다.")
    @Test
    void getRoleName(){
        String roleName = "role1";
        TeamRole teamRole1 = TeamRole.builder()
                .role(new Role(roleName))
                .build();

        assertThat(teamRole1.getRole().getName()).isEqualTo(roleName);
    }

}
