package com.artistry.artistry.service;

import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Dto.Response.TeamResponseDto;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Service.MemberService;
import com.artistry.artistry.Service.RoleService;
import com.artistry.artistry.Service.TagService;
import com.artistry.artistry.Service.TeamService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TeamServiceTest {
    @Autowired
    private TeamService teamService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoleService roleService;

    @DisplayName("팀 Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> teamService.findById(Long.MAX_VALUE))
                .isInstanceOf(TeamNotFoundException.class);
    }

    @DisplayName("요청한 Id의 방 정보를 반환.")
    @Transactional
    @Test
    void teamFindTest() {


        //given
        String tagName1 = "band";
        String tagName2 = "rock";

        Member member = memberService.findById(1L);
        Role role1 = roleService.findById(1L);
        Role role2 = roleService.findById(2L);

        Tag tag1 = tagRepository.save(new Tag(tagName1));
        Tag tag2 = tagRepository.save(new Tag(tagName2));

        List<Tag> tags = Arrays.asList(tag1,tag2);
        List<Role> roles = Arrays.asList(role1,role2);

        Team team = Team.builder()
                .name("team1")
                .roles(roles)
                .host(member)
                .tags(tags)
                .build();

        teamRepository.save(team);
        TeamResponseDto expected = TeamResponseDto.from(team);

        //when
        TeamResponseDto teamResponseDto = teamService.findById(team.getId());

        //then
        assertThat(teamResponseDto).usingRecursiveComparison()
                .ignoringFields("team", "createdAt").isEqualTo(expected);


    }
}