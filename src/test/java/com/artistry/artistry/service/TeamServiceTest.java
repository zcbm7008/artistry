package com.artistry.artistry.service;

import com.artistry.artistry.Domain.*;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Request.TagRequest;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.*;
import com.artistry.artistry.Service.MemberService;
import com.artistry.artistry.Service.RoleService;
import com.artistry.artistry.Service.TagService;
import com.artistry.artistry.Service.TeamService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TeamServiceTest {
    @Autowired
    private TeamService teamService;
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
    private TagService tagService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoleService roleService;
    @Autowired
    EntityManager entityManager;
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
        assertThat(responseDto.getRoles()).containsExactly(roleName1,roleName2);
        assertThat(responseDto.getTags()).containsExactly(tagName1,tagName2);
        assertThat(responseDto.getHost().getId()).isEqualTo(member1.getId());
        assertThat(responseDto.getCreatedAt()).isNotNull();
    }

    @DisplayName("요청한 팀 Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> teamService.findById(Long.MAX_VALUE))
                .isInstanceOf(TeamNotFoundException.class);
    }

    @DisplayName("요청한 팀 Id의 모든 지원서를 출력한다")
    @Test
    void showApprovedPortfolios(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String tagName1 = "밴드";
        String tagName2 = "락";
        Member member1 = memberRepository.save(new Member("member1"));
        Member member2 = memberRepository.save(new Member("member2"));
        Member applicant = memberRepository.save(new Member("applicant"));
        Role role1 = roleRepository.save(new Role(roleName1));
        Role role2 = roleRepository.save(new Role(roleName2));
        Tag tag1 = tagRepository.save(new Tag(tagName1));
        Tag tag2 = tagRepository.save(new Tag(tagName2));

        TagRequest tagRequest1 = new TagRequest(tag1.getId());
        TagRequest tagRequest2 = new TagRequest(tag2.getId());
        RoleRequest roleRequest1 = new RoleRequest(role1.getId());
        RoleRequest roleRequest2 = new RoleRequest(role2.getId());

        Portfolio portfolio1 = portfolioRepository.save(new Portfolio(1L,"portfolio1 for composer",role1,member2));
        Portfolio portfolio2 = portfolioRepository.save(new Portfolio(2L,"portfolio2 for drummer",role2,applicant));

        Team team = new Team(teamName,member1,Arrays.asList(tag1,tag2),Arrays.asList(role1,role2));

        teamRepository.save(team);



        Application application1 = applicationRepository.save(new Application(team,role1,team.findTeamRoleByRole(role1),member2,portfolio1));
        Application application2 = applicationRepository.save(new Application(team,role2,team.findTeamRoleByRole(role2),applicant,portfolio2));

        team.addApplicationInTeamRole(role1,application1);
        team.findTeamRoleByRole(role2).getApplications().add(application2);

        teamRepository.save(team);

//        FoundTeamResponse foundTeamResponse =teamService.findById(team.getId());
//
//
//        assertThat(foundTeamResponse.getTeamId()).isEqualTo(team.getId());
//        assertThat(foundTeamResponse.approvedPortfolios)
//                .extracting(PortfolioResponse::getRoleName)
//                .containsExactly(role1.getRoleName());

        List<PortfolioResponse> approvedPortfolios = teamService.getPortfoliosByStatus(team.getId(), ApplicationStatus.APPROVED);
        assertThat(approvedPortfolios)
                .extracting(PortfolioResponse::getRoleName)
                .containsExactly(role1.getRoleName());
        assertThat(approvedPortfolios)
                .extracting(PortfolioResponse::getMemberNickname)
                .containsExactly(member2.getNickname());

        List<PortfolioResponse> pendingPortfolios = teamService.getPortfoliosByStatus(team.getId(), ApplicationStatus.PENDING);
        assertThat(pendingPortfolios)
                .extracting(PortfolioResponse::getRoleName)
                .containsExactly(role2.getRoleName());
        assertThat(pendingPortfolios)
                .extracting(PortfolioResponse::getMemberNickname)
                .containsExactly(applicant.getNickname());
    }

//
//    @DisplayName("요청한 Id의 방 정보를 반환.")
//    @Transactional
//    @Test
//    void teamFindTest() {
//
//
//        //given
//        String tagName1 = "band";
//        String tagName2 = "rock";
//
//        Member member = memberService.findById(1L);
//        Role role1 = roleService.findById(1L);
//        Role role2 = roleService.findById(2L);
//
//        Tag tag1 = tagRepository.save(new Tag(tagName1));
//        Tag tag2 = tagRepository.save(new Tag(tagName2));
//
//        List<Tag> tags = Arrays.asList(tag1,tag2);
//        List<Role> roles = Arrays.asList(role1,role2);
//
//        Team team = Team.builder()
//                .name("team1")
//                .roles(roles)
//                .host(member)
//                .tags(tags)
//                .build();
//
//        teamRepository.save(team);
//        TeamResponseDto expected = TeamResponseDto.from(team);
//
//        //when
//        TeamResponseDto teamResponseDto = teamService.findById(team.getId());
//
//        //then
//        assertThat(teamResponseDto).usingRecursiveComparison()
//                .ignoringFields("team", "createdAt").isEqualTo(expected);
//
//
//    }
}