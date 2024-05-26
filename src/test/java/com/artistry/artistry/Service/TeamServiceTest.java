package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Request.TagRequest;
import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Exceptions.TeamRoleNotFoundException;
import com.artistry.artistry.Repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.*;
@Transactional
@SpringBootTest
public class TeamServiceTest {
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRoleService teamRoleService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    TeamRoleRepository teamRoleRepository;
    @Autowired
    ApplicationRepository applicationRepository;

    @DisplayName("팀을 생성한다")
    @Test
    void createTeam(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String tagName1 = "밴드";
        String tagName2 = "락";
        Member member1 = memberRepository.save(new Member("member1"));
        Role role1 = roleRepository.save(new Role(roleName1));
        Role role2 = roleRepository.save(new Role(roleName2));
        Tag tag1 = tagRepository.save(new Tag(tagName1));
        Tag tag2 = tagRepository.save(new Tag(tagName2));
        TagRequest tagRequest1 = new TagRequest(tag1.getId());
        TagRequest tagRequest2 = new TagRequest(tag2.getId());
        RoleRequest roleRequest1 = new RoleRequest(role1.getId());
        RoleRequest roleRequest2 = new RoleRequest(role2.getId());

        TeamRequest teamRequest = new TeamRequest(teamName,member1.getId(),Arrays.asList(tagRequest1,tagRequest2),Arrays.asList(roleRequest1,roleRequest2));
        TeamResponse responseDto = teamService.create(teamRequest);

        assertThat(responseDto.getTeamId()).isNotNull();
        assertThat(responseDto.getTeamRoles())
                .extracting(teamRole -> teamRole.getRole().getName())
                .containsExactly(roleName1, roleName2);
        assertThat(responseDto.getTags()).containsExactly(tagName1,tagName2);
        assertThat(responseDto.getHost().getId()).isEqualTo(member1.getId());
        assertThat(responseDto.getCreatedAt()).isNotNull();
    }

    @DisplayName("팀에 없는 역할에 대한 지원서를 신청 때 예외를 던진다.")
    @Test
    void applicationRoleNotInTeam(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String invalidRoleName = "일러스트레이터";
        String tagName1 = "밴드";
        Member member1 = memberRepository.save(new Member("member1"));
        Member member2 = memberRepository.save(new Member("member2"));
        Member applicant1 = memberRepository.save(new Member("applicant1"));


        Role role1 = roleRepository.save(new Role(roleName1));
        Role invalidRole = roleRepository.save(new Role(invalidRoleName));

        Tag tag1 = tagRepository.save(new Tag(tagName1));

        Portfolio portfolio1 = portfolioRepository.save(new Portfolio( "portfolio1 for composer", role1));
        Portfolio portfolio2 = portfolioRepository.save(new Portfolio( "portfolio2 for drummer", invalidRole));

        Team team = new Team(teamName, member1, List.of(tag1), List.of(role1));

        teamRepository.save(team);

        Application application1 = applicationRepository.save(new Application(team, role1, member2, portfolio1));
        Application application2 = applicationRepository.save(new Application(team, invalidRole, applicant1, portfolio2));

        team.findTeamRoleByRole(role1).addApplication(application1);
        assertThatThrownBy(() -> team.findTeamRoleByRole(invalidRole).getApplications().add(application2)).isInstanceOf(TeamRoleNotFoundException.class);

    }

    @DisplayName("요청한 팀 Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> teamService.findById(Long.MAX_VALUE))
                .isInstanceOf(TeamNotFoundException.class);
    }



}