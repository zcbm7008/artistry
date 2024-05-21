package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.*;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Exceptions.TeamRoleNotFoundException;
import com.artistry.artistry.Repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TeamRoleServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    TeamRoleService teamRoleService;

    @DisplayName("요청한 팀 역할 Id의 모든 지원서를 출력한다")
    @Test
    void showApprovedPortfolios() {
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String tagName1 = "밴드";
        String tagName2 = "락";
        Member member1 = memberRepository.save(new Member("member1"));
        Member member2 = memberRepository.save(new Member("member2"));
        Member applicant1 = memberRepository.save(new Member("applicant1"));
        Member applicant2 = memberRepository.save(new Member("applicant2"));
        Role role1 = roleRepository.save(new Role(roleName1));
        Role role2 = roleRepository.save(new Role(roleName2));
        Tag tag1 = tagRepository.save(new Tag(tagName1));
        Tag tag2 = tagRepository.save(new Tag(tagName2));

        Portfolio portfolio1 = portfolioRepository.save(new Portfolio(1L, "portfolio1 for composer", role1, member2));
        Portfolio portfolio2 = portfolioRepository.save(new Portfolio(2L, "portfolio2 for drummer", role2, applicant1));
        Portfolio portfolio3 = portfolioRepository.save(new Portfolio(3L, "portfolio3 for drummer", role2, applicant2));

        Team team = new Team(teamName, member1, Arrays.asList(tag1, tag2), Arrays.asList(role1, role2));

        teamRepository.save(team);

        Application application1 = applicationRepository.save(new Application(team, role1, team.findTeamRoleByRole(role1), member2, portfolio1));
        Application application2 = applicationRepository.save(new Application(team, role2, team.findTeamRoleByRole(role2), applicant1, portfolio2));
        Application application3 = applicationRepository.save(new Application(team, role2, team.findTeamRoleByRole(role2), applicant2, portfolio3));

        team.findTeamRoleByRole(role1).addApplication(application1);
        team.findTeamRoleByRole(role2).getApplications().add(application2);
        team.findTeamRoleByRole(role2).getApplications().add(application3);

        teamRepository.save(team);

        List<ApplicationResponse> role1Applications = teamRoleService.getApplications(team.findTeamRoleByRole(role1).getId());
        List<ApplicationResponse> role2Applications = teamRoleService.getApplications(team.findTeamRoleByRole(role2).getId());

        assertThat(role1Applications)
                .extracting(ApplicationResponse::getPortfolio)
                .map(PortfolioResponse::getId)
                .containsExactly(portfolio1.getId());

        assertThat(role2Applications)
                .extracting(ApplicationResponse::getPortfolio)
                .map(PortfolioResponse::getId)
                .containsExactly(portfolio2.getId(),portfolio3.getId());
    }

    @DisplayName("요청한 팀 Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> teamRoleService.findById(Long.MAX_VALUE))
                .isInstanceOf(TeamRoleNotFoundException.class);
    }
}