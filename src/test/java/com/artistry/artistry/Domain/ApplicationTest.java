package com.artistry.artistry.Domain;

import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class ApplicationTest {

    private Team team;
    private List<Tag> tags;
    private List<Role> roles;
    private Member host;
    private Member member;
    private Portfolio portfolio;
    private Application application;
    private Role appliedRole;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    TeamRepository teamRepository;

    @BeforeEach
    public void setUp(){
        String teamName = "밴드 팀";
        String roleName1 = "작곡가";
        String roleName2 = "드럼";
        String tagName1 = "밴드";
        String tagName2 = "락";
        Member member1 = memberRepository.save(new Member("member1"));
        List <Role> roles = Arrays.asList( roleRepository.save(new Role(roleName1)), roleRepository.save(new Role(roleName2)));
        List <Tag> tags = Arrays.asList(tagRepository.save(new Tag(tagName1)),tagRepository.save(new Tag(tagName2)));

        team = teamRepository.save(new Team(teamName,member1,tags,roles));

        appliedRole = roles.get(0);

        application = Application.builder()
                .team(team)
                .role(appliedRole)
                .member(member)
                .status(ApplicationStatus.PENDING)
                .build();

    }

    @Test
    @DisplayName("팀의 특정한 역할에 지원서를 지원한다.")
    public void applyToTeam(){

        team.apply(application);
        assertEquals(application.getId(),team.getTeamRoles().get(0).getApplications().get(0).getId());
        assertEquals(application.getTeam().getId(),team.getId());
        assertEquals(application.getRole(),team.findTeamRoleByRole(appliedRole).getRole());

    }
    @Test
    @DisplayName("지원한 application의 상태는 PENDING이어야 한다.")
    public void isStatusPending(){
        team.apply(application);
        assertEquals(application.getStatus(),ApplicationStatus.PENDING);

    }
}
