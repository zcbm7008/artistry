package com.artistry.artistry.service;

import com.artistry.artistry.Domain.*;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Request.TagRequest;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Service.MemberService;
import com.artistry.artistry.Service.RoleService;
import com.artistry.artistry.Service.TagService;
import com.artistry.artistry.Service.TeamService;
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
    private TagService tagService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoleService roleService;

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
        assertThat(responseDto.getMembers()).isNull();
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

    @DisplayName("요청한 팀 Id의 모든 포트폴리오를 출력한다")
    @Test
    void showApprovedPortfolios(){

        List <PortfolioResponse> approvedPortfolios = teamService.getApprovedPortfolios(1L);
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