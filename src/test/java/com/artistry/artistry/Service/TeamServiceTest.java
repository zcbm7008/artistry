package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.*;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Request.TagRequest;
import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;



import static org.assertj.core.api.Assertions.*;

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
                .extracting(teamRole -> teamRole.getRole().getRoleName())
                .containsExactly(roleName1, roleName2);
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



}