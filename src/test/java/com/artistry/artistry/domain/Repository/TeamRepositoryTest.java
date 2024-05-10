package com.artistry.artistry.domain.Repository;


import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Service.MemberService;
import com.artistry.artistry.Service.RoleService;
import com.artistry.artistry.Service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MemberService memberService;

    @DisplayName("팀 더미데이터 확인")
    @ParameterizedTest
    @CsvSource({"1,team1","2,team2"})
    void dummyGroupTest(Long id, String groupName){
        Optional<Team> team = teamRepository.findById(id);
        assertThat(team.isPresent()).isTrue();
        assertThat(team.get().getName()).isEqualTo(groupName);
    }

    @DisplayName("생성한 팀을 저장한다")
    @Test
    void saveTest() {
        Team team = saveTeam();
        teamRepository.flush();

        assertThat(teamRepository.existsById(team.getId())).isTrue();


    }

    private Team saveTeam(){
        List<Role> roles = Arrays.asList(roleService.findById(1L),
                roleService.findById(2L));

        List<Member> members = Arrays.asList(memberService.findById(1L),
                memberService.findById(2L));

        List<Tag> tags = Arrays.asList(tagService.findById(1L),
                tagService.findById(2L));

        return teamRepository.save(Team.builder()
                .name("team1")
                .host(memberService.findById(1L))
                .roles(roles)
                .applicants(members)
                .tags(tags)
                .build());
    }

    @DisplayName("팀 생성시각을 저장한다.")
    @Test
    void saveTeamCreatedDate() {
        Team team = saveTeam();

        assertThat(team.getCreatedAt()).isNotNull();
    }


}
