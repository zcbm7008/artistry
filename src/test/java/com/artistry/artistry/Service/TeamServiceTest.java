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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        Member member1 = memberRepository.save(new Member("member1","a@a.com"));
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
        Member member1 = memberRepository.save(new Member("member1","a@a.com"));
        Member member2 = memberRepository.save(new Member("member2","b@b.com"));
        Member applicant1 = memberRepository.save(new Member("applicant1","c@c.com"));


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

    @DisplayName("팀 이름으로 팀을 검색한다.")
    @Test
    void findTeamByName(){
        Member host = memberRepository.save(new Member("host","host@host.com","hosturl"));
        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));
        Role role1 = roleRepository.save(new Role("role1"));

        List<Tag> tagList = Arrays.asList(tag1,tag2);
        String nameToFind = "공모전";

        Team team1 =
                Team.builder()
                        .name(nameToFind + "참여하실분12312312")
                        .roles(List.of(role1))
                        .tags(tagList)
                        .host(host).build();

        Team team2 =
                Team.builder()
                        .name(nameToFind + "참여하실분1233434")
                        .roles(List.of(role1))
                        .tags(Arrays.asList(tag1,tag2,tag3))
                        .host(host).build();

        teamRepository.save(team1);
        teamRepository.save(team2);
        
        List <TeamResponse> response = teamService.findTeamsByNameLike("공모전");

        assertThat(response).hasSize(2)
                .extracting(TeamResponse::getTeamName)
                .allMatch(name -> name.contains(nameToFind));

    }

    @DisplayName("태그 Id로 팀을 검색한다.")
    @Test
    void findTeamByTagIds() {
        Member host = memberRepository.save(new Member("host","host@host.com","hosturl"));
        Tag tag1 = tagRepository.save(new Tag("tag1"));
        Tag tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));
        Role role1 = roleRepository.save(new Role("role1"));

        List<Tag> tagList = Arrays.asList(tag1,tag2);
        List<String> tagNames = tagList.stream().map(Tag::getName).toList();
        List<Long> tagIds = tagList.stream().map(Tag::getId).toList();

        List<String> tagIdsAsString = tagIds.stream()
                .map(String::valueOf)
                .toList();

        Team team1 =
                Team.builder()
                        .name("team1")
                        .roles(List.of(role1))
                        .tags(tagList)
                        .host(host).build();

        Team team2 =
                Team.builder()
                        .name("team1")
                        .roles(List.of(role1))
                        .tags(Arrays.asList(tag1,tag2,tag3))
                        .host(host).build();

        teamRepository.save(team1);
        teamRepository.save(team2);

        List<TeamResponse> responses = teamService.findTeamsByTagIds(tagIds);
        assertThat(responses).hasSize(2)
                .extracting(TeamResponse::getTags).contains(tagNames);
    }

    @DisplayName("요청한 팀 Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> teamService.findById(Long.MAX_VALUE))
                .isInstanceOf(TeamNotFoundException.class);
    }



}