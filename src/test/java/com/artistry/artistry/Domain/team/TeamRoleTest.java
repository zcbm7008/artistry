package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class TeamRoleTest {
    String title1;
    String title2;
    Application application1;
    Application application2;
    TeamRole teamRole1;
    Member member1;
    Member member2;

    @BeforeEach
    void setUp(){
        member1 = new Member("member1","a@a.com","as.url");
        member2 = new Member("member2","b@b.com","as.url");
        title1 = "title1";
        title2 = "title2";
        Role role1 = new Role("role1");
        Portfolio portfolio1 = new Portfolio(member1,title1,role1);
        Portfolio portfolio2 = new Portfolio(member2,title2,role1);

        teamRole1 = TeamRole.builder()
                .role(role1)
                .applications(new ArrayList<>())
                .build();

        application1 = teamRole1.applyPortfolio(portfolio1);
        application1.setStatus(ApplicationStatus.APPROVED);

        application2 = teamRole1.applyPortfolio(portfolio2);
        application2.setStatus(ApplicationStatus.REJECTED);
    }

    @DisplayName("teamRole의 모든 포트폴리오를 가져온다.")
    @Test
    void getAllPortfolios(){
        List <Portfolio> portfolios = teamRole1.getAllPortfolios();
        assertThat(portfolios).hasSize(2).contains(application1.getPortfolio(),application2.getPortfolio());
    }

    @DisplayName("teamRole의 포트폴리오를 Application Status에 따라 가져온다.")
    @Test
    void getTeamRolePortfolios(){
        List<Portfolio> approvedPortfolios = teamRole1.getPortfoliosByStatus(ApplicationStatus.APPROVED);
        List<Portfolio> rejectedPortfolios = teamRole1.getPortfoliosByStatus(ApplicationStatus.REJECTED);
        List<Portfolio> pendingPortfolios = teamRole1.getPortfoliosByStatus(ApplicationStatus.PENDING);

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
                .applications(new ArrayList<>())
                .build();

        assertThat(teamRole1.getRole().getName()).isEqualTo(roleName);
    }

    @DisplayName("TeamRole에 Approved된 Application의 여부에 따라 Boolean을 반환한다.")
    @Test
    void isApprovedInTeamRole(){
        assertThat(teamRole1.isApprovedInTeamRole()).isTrue();

        application1.setStatus(ApplicationStatus.REJECTED);
        assertThat(teamRole1.isApprovedInTeamRole()).isFalse();
    }

    @DisplayName("TeamRole에 Approved된 Application만 남긴다.")
    @Test
    void getOnlyApprovedApplications(){
        application1.setStatus(ApplicationStatus.APPROVED);
        teamRole1.filterApprovedApplications();

        assertThat(teamRole1.getApplications()).hasSize(1).containsExactly(application1);

    }

    @DisplayName("TeamRole의 모든 Application을 제거한다.")
    @Test
    public void removeAllApplications(){
        teamRole1.removeAllApplications();
        assertThat(teamRole1.getApplications()).hasSize(0);
    }

}
