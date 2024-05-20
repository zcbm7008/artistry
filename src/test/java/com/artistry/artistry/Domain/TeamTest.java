package com.artistry.artistry.Domain;

import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamTest {

    private Team team;
    private List<Tag> tags;
    private List<Role> roles;
    private Member host;
    private Member member;
    private Portfolio portfolio;
    private Application application;
    private Role appliedRole;
    @BeforeEach
    public void setUp(){
        tags= Arrays.asList(
                Tag.of("밴드"),
                Tag.of("락")
        );

        roles = Arrays.asList(
                Role.of("작곡가"),
                Role.of("보컬")
        );

        host = Member.builder()
                .nickname("호스트")
                .build();

        member = Member.builder()
                .nickname("멤버1")
                .build();

        team = Team.builder()
                .name("밴드")
                .tags(tags)
                .host(host)
                .build();

        team.addRoles(roles);

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

        team.apply(team.getTeamRoles().get(0), application);
        assertEquals(application.getId(),team.getTeamRoles().get(0).getApplications().get(0).getId());
        assertEquals(application.getTeam().getId(),team.getId());
        assertEquals(application.getRole(),team.findTeamRoleByRole(appliedRole).getRole());

    }
    @Test
    @DisplayName("지원한 application의 상태는 PENDING이어야 한다.")
    public void isStatusPending(){
        team.apply(team.getTeamRoles().get(0), application);
        assertEquals(application.getStatus(),ApplicationStatus.PENDING);

    }



}
