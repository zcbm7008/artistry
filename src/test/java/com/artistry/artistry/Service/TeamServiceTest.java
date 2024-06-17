package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamRole;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Exceptions.TeamNotRecruitingException;
import com.artistry.artistry.Exceptions.TeamRoleHasApprovedException;
import com.artistry.artistry.Exceptions.TeamRoleNotFoundException;
import com.artistry.artistry.Repository.*;
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
    private RoleService roleService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    ApplicationRepository applicationRepository;

    private TagRequest createTagRequest(String name){
        Tag tag = tagRepository.save(new Tag(name));
        return new TagRequest(tag.getId());
    }

    private RoleRequest createRoleRequest(String name){
        Role role = roleRepository.save(new Role(name));
        return new RoleRequest(role.getId());
    }


    @DisplayName("팀을 생성한다")
    @Test
    void createTeam(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String tagName1 = "밴드";
        String tagName2 = "락";
        Member member1 = memberRepository.save(new Member("member1","a@a.com"));

        TagRequest tagRequest1 = createTagRequest(tagName1);
        TagRequest tagRequest2 = createTagRequest(tagName2);
        RoleRequest roleRequest1 = createRoleRequest(roleName1);
        RoleRequest roleRequest2 = createRoleRequest(roleName2);

        TeamRequest teamRequest =
                new TeamRequest(
                        teamName,
                        member1.getId(),
                        Arrays.asList(tagRequest1,tagRequest2),
                        Arrays.asList(roleRequest1,roleRequest2));

        TeamResponse responseDto = teamService.create(teamRequest);

        assertThat(responseDto.getId()).isNotNull();
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

        Portfolio portfolio1 = portfolioRepository.save(new Portfolio(member1, "portfolio1 for composer", role1));
        Portfolio portfolio2 = portfolioRepository.save(new Portfolio(member2, "portfolio2 for drummer", invalidRole));

        Team team = teamRepository.save(new Team(teamName, member1, List.of(tag1), List.of(role1)));

        team.apply(portfolio1);

        assertThatThrownBy(() -> team.apply(portfolio2)).isInstanceOf(TeamRoleNotFoundException.class);

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
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        Team team2 =
                Team.builder()
                        .name(nameToFind + "참여하실분1233434")
                        .roles(List.of(role1))
                        .tags(Arrays.asList(tag1,tag2,tag3))
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        teamRepository.save(team1);
        teamRepository.save(team2);
        
        List <TeamResponse> response = teamService.findTeamsByNameLike("공모전");

        assertThat(response).hasSize(2)
                .extracting(TeamResponse::getName)
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
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        Team team2 =
                Team.builder()
                        .name("team1")
                        .roles(List.of(role1))
                        .tags(Arrays.asList(tag1,tag2,tag3))
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        teamRepository.save(team1);
        teamRepository.save(team2);

        List<TeamResponse> responses = teamService.findTeamsByTagIds(tagIds);
        assertThat(responses).hasSize(2)
                .extracting(TeamResponse::getTags).contains(tagNames);
    }

    @DisplayName("역할 Id로 팀을 검색한다.")
    @Test
    void findTeamByRoleIds() {
        Member host = memberRepository.save(new Member("host","host@host.com","hosturl"));

        Role role1 = roleRepository.save(new Role("작곡가"));
        Role role2 = roleRepository.save(new Role("드럼"));

        List<Role> roleList = Arrays.asList(role1,role2);
        List<String> roleNames = roleList.stream().map(Role::getName).toList();
        List<Long> roleIds = roleList.stream().map(Role::getId).toList();

        Team team1 =
                Team.builder()
                        .name("team1")
                        .roles(List.of(role1,role2))
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        Team team2 =
                Team.builder()
                        .name("team1")
                        .roles(List.of(role1))
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        teamRepository.save(team1);
        teamRepository.save(team2);

        List<TeamResponse> responses = teamService.findTeamsByRoleIds(roleIds);
        assertThat(responses).hasSize(2)
                .extracting(TeamResponse::getRoleNames).contains(roleNames);
    }

    @DisplayName("Approved 된 Application의 Member로 Team을 검색한다.")
    @Test
    void findTeamsByApprovedApplicationMember(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "베이스";
        String tagName1 = "밴드";

        Member host = memberRepository.save(new Member("host","host@host.com","hosturl"));
        Member member1 = memberRepository.save(new Member("member1","a@a.com"));

        Role role1 = roleRepository.save(new Role(roleName1));
        Role role2 = roleRepository.save(new Role(roleName2));

        Tag tag1 = tagRepository.save(new Tag(tagName1));

        Portfolio portfolio1 = portfolioRepository.save(new Portfolio(member1, "portfolio1 for composer", role1));
        Portfolio portfolio2 = portfolioRepository.save(new Portfolio(member1, "portfolio2 for drummer", role2));

        Team team1 = new Team(teamName, host, List.of(tag1), List.of(role1));
        Team team2 = new Team(teamName, host, List.of(tag1), List.of(role1,role2));

        teamRepository.save(team1);
        teamRepository.save(team2);

        ApplicationResponse application1 = teamService.apply(team1.getId(),new PortfolioRequest(portfolio1.getId()));
        ApplicationResponse application2 = teamService.apply(team2.getId(),new PortfolioRequest(portfolio2.getId()));

        applicationService.changedApplicationStatus(application1.getId(),team1.getHost().getId(),ApplicationStatus.APPROVED);
        applicationService.changedApplicationStatus(application2.getId(),team1.getHost().getId(),ApplicationStatus.APPROVED);

        List <TeamResponse> responses = teamService.findTeamsByApprovedMember(member1.getId());

        assertThat(responses).hasSize(2);
    }

    @DisplayName("팀 정보를 수정할 때, 역할에 승인된 지원자가 없으면 수정을 실행한다.")
    @Test
    void update(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String roleName3 = "베이스";

        String tagName1 = "밴드";
        String tagName2 = "락";
        String tagName3 = "인디";

        Member member1 = memberRepository.save(new Member("member1","a@a.com"));

        TagRequest tagRequest1 = createTagRequest(tagName1);
        TagRequest tagRequest2 = createTagRequest(tagName2);
        TagRequest tagRequest3 = createTagRequest(tagName3);

        RoleRequest roleRequest1 = createRoleRequest(roleName1);
        RoleRequest roleRequest2 = createRoleRequest(roleName2);
        RoleRequest roleRequest3 = createRoleRequest(roleName3);

        TeamRequest teamRequest =
                new TeamRequest(
                        teamName,
                        member1.getId(),
                        Arrays.asList(tagRequest1,tagRequest2),
                        Arrays.asList(roleRequest1,roleRequest2));

        TeamResponse responseDto = teamService.create(teamRequest);

        String changedName = "밴드팀팀팀";

        TeamUpdateRequest request =
                TeamUpdateRequest.builder()
                        .name(changedName)
                        .tags(List.of(tagRequest1,tagRequest3))
                        .roles(List.of(roleRequest2,roleRequest3))
                        .teamStatus(String.valueOf(TeamStatus.RECRUITING))
                        .build();

        TeamResponse response = teamService.update(responseDto.getId(),request);

        assertThat(response.getName()).isEqualTo(changedName);
        assertThat(response.getTags()).containsExactly(tagName1,tagName3);
        assertThat(response.getRoleNames()).containsExactly(roleName2,roleName3);
        assertThat(response.getTeamStatus()).isEqualTo(String.valueOf(TeamStatus.RECRUITING));

    }
    @DisplayName("역할에 승인된 지원자가 있으면, Role을 수정할 때 예외를 출력한다.")
    @Test
    void exceptionWhenApprovedInTeamRole(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String roleName3 = "베이스";

        String tagName1 = "밴드";
        String tagName2 = "락";
        String tagName3 = "인디";

        Member member1 = memberRepository.save(new Member("member1","a@a.com"));

        TagRequest tagRequest1 = createTagRequest(tagName1);
        TagRequest tagRequest2 = createTagRequest(tagName2);
        TagRequest tagRequest3 = createTagRequest(tagName3);

        RoleRequest roleRequest1 = createRoleRequest(roleName1);
        RoleRequest roleRequest2 = createRoleRequest(roleName2);
        RoleRequest roleRequest3 = createRoleRequest(roleName3);

        TeamRequest teamRequest =
                new TeamRequest(
                        teamName,
                        member1.getId(),
                        Arrays.asList(tagRequest1,tagRequest2),
                        Arrays.asList(roleRequest1,roleRequest2));

        TeamResponse responseDto = teamService.create(teamRequest);

        String changedName = "밴드팀팀팀";

        Role role1 = roleService.findEntityById(roleRequest1.getId());
        Portfolio portfolio = portfolioRepository.save(new Portfolio(member1,"포폴1", role1));
        Team team = teamService.findEntityById(responseDto.getId());

        ApplicationResponse response = teamService.apply(responseDto.getId(), new PortfolioRequest(portfolio.getId()));
        Application application = applicationService.findEntityById(response.getId());
        application.setStatus(ApplicationStatus.APPROVED);

        TeamUpdateRequest request =
                TeamUpdateRequest.builder()
                        .name(changedName)
                        .tags(List.of(tagRequest1,tagRequest3))
                        .roles(List.of(roleRequest2,roleRequest3))
                        .teamStatus(String.valueOf(TeamStatus.CANCELED))
                        .build();

        assertThatThrownBy(() -> teamService.update(responseDto.getId(),request)).isInstanceOf(TeamRoleHasApprovedException.class);

    }

    @DisplayName("Team의 상태가 Cancel이 되면, apply를 중단하고, 모든 application을 삭제한다.")
    @Test
    void cancelTeam(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "베이스";
        String tagName1 = "밴드";

        Member host = memberRepository.save(new Member("host","host@host.com","hosturl"));
        Member member1 = memberRepository.save(new Member("member1","a@a.com"));

        Role role1 = roleRepository.save(new Role(roleName1));
        Role role2 = roleRepository.save(new Role(roleName2));

        Tag tag1 = tagRepository.save(new Tag(tagName1));

        Portfolio portfolio1 = portfolioRepository.save(new Portfolio(member1,"portfolio1 for composer", role1));
        Portfolio portfolio2 = portfolioRepository.save(new Portfolio(member1, "portfolio2 for drummer", role2));

        Team team1 = new Team(teamName, host, List.of(tag1), List.of(role1));

        teamRepository.save(team1);

        ApplicationResponse applicationResponse1 = teamService.apply(team1.getId(),new PortfolioRequest(portfolio1.getId()));

        applicationService.changedApplicationStatus(applicationResponse1.getId(),team1.getHost().getId(), ApplicationStatus.APPROVED);

        //when
        team1.cancel();

        //then
        assertThatThrownBy(()->team1.apply(portfolio2)).isInstanceOf(TeamNotRecruitingException.class);

        assertThat(team1.getTeamRoles().stream()
                .allMatch(teamRole -> teamRole.getApplications().isEmpty()))
                .isTrue();
    }

    @DisplayName("Team의 상태가 Finish가 되면, apply를 중단하고, 승인된 application만 저장한다.")
    @Test
    void finishTeam(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "베이스";
        String tagName1 = "밴드";

        Member host = memberRepository.save(new Member("host","host@host.com","hosturl"));
        Member member1 = memberRepository.save(new Member("member1","a@a.com"));
        Member member2 = memberRepository.save(new Member("member2","a@a.com"));

        Role role1 = roleRepository.save(new Role(roleName1));
        Role role2 = roleRepository.save(new Role(roleName2));

        Tag tag1 = tagRepository.save(new Tag(tagName1));

        Portfolio portfolio1 = portfolioRepository.save(new Portfolio(member1, "portfolio1 for composer", role1));
        Portfolio portfolio2 = portfolioRepository.save(new Portfolio(member1, "portfolio2 for drummer", role2));
        Portfolio portfolio3 = portfolioRepository.save(new Portfolio(member2, "portfolio3 for drummer", role2));

        Team team1 = new Team(teamName, host, List.of(tag1), List.of(role1,role2));

        teamRepository.save(team1);

        ApplicationResponse applicationResponse = teamService.apply(team1.getId(),new PortfolioRequest(portfolio1.getId()));
        team1.apply(portfolio2);

        applicationService.changedApplicationStatus(applicationResponse.getId(),team1.getHost().getId(),ApplicationStatus.APPROVED);

        //when
        TeamRole teamRole1 = team1.findTeamRoleByRole(role1);
        TeamRole teamRole2 = team1.findTeamRoleByRole(role2);

        team1.finish();

        //then
        assertThatThrownBy(()->team1.apply(portfolio3)).isInstanceOf(TeamNotRecruitingException.class);
        assertThat(teamRole1.getApplications().stream()
                .map(Application::getStatus)
                .allMatch(status -> status == ApplicationStatus.APPROVED)).isTrue();
        assertThat(teamRole2.getApplications()).hasSize(0);
    }


    @DisplayName("요청한 팀 Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> teamService.findById(Long.MAX_VALUE))
                .isInstanceOf(TeamNotFoundException.class);
    }

}