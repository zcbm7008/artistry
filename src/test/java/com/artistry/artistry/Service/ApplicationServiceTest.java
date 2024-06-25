package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.application.ApplicationType;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.ApplicationCreateRequest;
import com.artistry.artistry.Dto.Request.PortfolioRequest;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Request.TeamInfoRequest;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Exceptions.ArtistryUnauthorizedException;
import com.artistry.artistry.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class ApplicationServiceTest {
    @Autowired
    ApplicationService applicationService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PortfolioRepository portfolioRepository;

    Team team1;
    Member host;
    Role role1;
    Member applicant;
    Member otherMember;
    Portfolio portfolio;

    @BeforeEach
    void setUp(){
        host = memberRepository.save(new Member("host","host@host.com","hosturl"));
        applicant = memberRepository.save(new Member("applicant","applicant@applicant.com","applicanturl"));
        otherMember = memberRepository.save(new Member("otherMember","other@applicant.com","applicanturl"));
        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        role1 = roleRepository.save(new Role("role1"));

        List<Tag> tagList = Arrays.asList(tag1,tag2);


        Team team =
                Team.builder()
                        .name("team1")
                        .roles(List.of(role1))
                        .tags(tagList)
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        team1 = teamRepository.save(team);

        portfolio = portfolioRepository.save(new Portfolio(applicant,"title1",role1));

    }

    @DisplayName("지원서를 생성한다.")
    @Test
    void createApplication(){
        ApplicationCreateRequest request =
                ApplicationCreateRequest.builder()
                        .team(new TeamInfoRequest(team1.getId()))
                        .portfolio(new PortfolioRequest(portfolio.getId()))
                        .role(new RoleRequest(role1.getId()))
                        .status(String.valueOf(ApplicationStatus.PENDING))
                        .type(String.valueOf(ApplicationType.APPLICATION))
                        .build();

        ApplicationResponse response = applicationService.createApplication(applicant.getId(),request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTeamId()).isEqualTo(team1.getId());
        assertThat(response.getPortfolio().getId()).isEqualTo(portfolio.getId());
        assertThat(response.getRole()).isEqualTo(role1.getName());
        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.PENDING.toString());
        assertThat(response.getType()).isEqualTo(ApplicationType.APPLICATION.toString());
    }

    @DisplayName("제안서를 생성한다.")
    @Test
    void createInvitation(){
        ApplicationCreateRequest request =
                ApplicationCreateRequest.builder()
                        .team(new TeamInfoRequest(team1.getId()))
                        .portfolio(new PortfolioRequest(portfolio.getId()))
                        .role(new RoleRequest(role1.getId()))
                        .status(String.valueOf(ApplicationStatus.PENDING))
                        .type(String.valueOf(ApplicationType.INVITATION))
                        .build();

        ApplicationResponse response = applicationService.createApplication(host.getId(), request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTeamId()).isEqualTo(team1.getId());
        assertThat(response.getPortfolio().getId()).isEqualTo(portfolio.getId());
        assertThat(response.getRole()).isEqualTo(role1.getName());
        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.PENDING.toString());
        assertThat(response.getType()).isEqualTo(ApplicationType.INVITATION.toString());
    }

    @DisplayName("지원서를 생성할 때, 주어진 Id가 포트폴리오의 소유자가 아닐경우 예외를 출력한다.")
    @Test
    void invitationExceptionWhenNotOwner(){
        ApplicationCreateRequest request =
                ApplicationCreateRequest.builder()
                        .team(new TeamInfoRequest(team1.getId()))
                        .portfolio(new PortfolioRequest(portfolio.getId()))
                        .role(new RoleRequest(role1.getId()))
                        .status(String.valueOf(ApplicationStatus.PENDING))
                        .type(String.valueOf(ApplicationType.INVITATION))
                        .build();

        assertThatThrownBy(() -> applicationService.createApplication(otherMember.getId(), request))
                .isInstanceOf(ArtistryUnauthorizedException.class);

    }

    @DisplayName("제안서를 생성할 때, 주어진 Id가 팀의 host가 아닐경우 예외를 출력한다.")
    @Test
    void invitationExceptionWhenNotHost(){
        ApplicationCreateRequest request =
                ApplicationCreateRequest.builder()
                        .team(new TeamInfoRequest(team1.getId()))
                        .portfolio(new PortfolioRequest(portfolio.getId()))
                        .role(new RoleRequest(role1.getId()))
                        .status(String.valueOf(ApplicationStatus.PENDING))
                        .type(String.valueOf(ApplicationType.INVITATION))
                        .build();

        assertThatThrownBy(() -> applicationService.createApplication(applicant.getId(), request))
                .isInstanceOf(ArtistryUnauthorizedException.class);

    }
}
