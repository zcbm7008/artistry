package com.artistry.artistry.Domain.Repository;


import com.artistry.artistry.Domain.*;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Request.TagRequest;
import com.artistry.artistry.Dto.Request.TeamRequest;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
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
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private RoleRepository roleRepository;

//    @DisplayName("팀 더미데이터 확인")
//    @ParameterizedTest
//    @CsvSource({"1,team1","2,team2"})
//    void dummyGroupTest(Long id, String groupName){
//        Optional<Team> team = teamRepository.findById(id);
//        assertThat(team.isPresent()).isTrue();
//        assertThat(team.get().getName()).isEqualTo(groupName);
//    }

    @DisplayName("팀을 생성한다")
    @Test
    void createTeam(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String tagName1 = "밴드";
        String tagName2 = "락";
        Member member1 = memberRepository.save(new Member("member1"));
        List <Role> roles = Arrays.asList( roleRepository.save(new Role(roleName1)), roleRepository.save(new Role(roleName2)));
        List <Tag> tags = Arrays.asList(tagRepository.save(new Tag(tagName1)),tagRepository.save(new Tag(tagName2)));

        Team team = teamRepository.save(new Team(teamName,member1,tags,roles));

        assertThat(team.getId()).isNotNull();
        assertThat(team.getTags()).containsExactlyInAnyOrderElementsOf(tags);
        assertThat(team.getTeamRoles().stream().map(TeamRole::getRole).collect(Collectors.toList())).containsExactlyInAnyOrderElementsOf(roles);

    }

    @DisplayName("팀 생성시각을 저장한다.")
    @Test
    void saveTeamCreatedDate() {
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String tagName1 = "밴드";
        String tagName2 = "락";
        Member member1 = memberRepository.save(new Member("member1"));
        List <Role> roles = Arrays.asList( roleRepository.save(new Role(roleName1)), roleRepository.save(new Role(roleName2)));
        List <Tag> tags = Arrays.asList(tagRepository.save(new Tag(tagName1)),tagRepository.save(new Tag(tagName2)));

        Team team = teamRepository.save(new Team(teamName,member1,tags,roles));
        assertThat(team.getCreatedAt()).isNotNull();
    }


}
