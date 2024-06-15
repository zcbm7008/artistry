package com.artistry.artistry.Domain.application;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Transactional
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
        Member member1 = memberRepository.save(new Member("member1","a@a.com"));
        List <Role> roles = Arrays.asList( roleRepository.save(new Role(roleName1)), roleRepository.save(new Role(roleName2)));
        List <Tag> tags = Arrays.asList(tagRepository.save(new Tag(tagName1)),tagRepository.save(new Tag(tagName2)));

        team = teamRepository.save(new Team(teamName,member1,tags,roles));

        portfolio =new Portfolio(member1,"title1",roles.get(0));

        appliedRole = roles.get(0);

        team.addRoles(List.of(appliedRole));

        application = new Application(team.findTeamRoleByRole(appliedRole),portfolio);

    }

    @Test
    @DisplayName("팀의 특정한 역할에 지원서를 지원한다.")
    public void applyToTeam(){
        team.apply(portfolio);
        assertEquals(application.getId(),team.getTeamRoles().get(0).getApplications().get(0).getId());
        assertEquals(application.getTeam().getId(),team.getId());
        assertEquals(application.getRole(),team.findTeamRoleByRole(appliedRole).getRole());

    }
    @Test
    @DisplayName("지원한 application의 상태는 PENDING이어야 한다.")
    public void isStatusPending(){
        team.apply(portfolio);
        assertEquals(application.getStatus(),ApplicationStatus.PENDING);
    }
}
