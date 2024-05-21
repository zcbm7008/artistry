package com.artistry.artistry.Domain;

import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
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
                new Tag("밴드"),
                new Tag("락")
        );

        roles = Arrays.asList(
                new Role("작곡가"),
                new Role("보컬")
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

//    @DisplayName("요청한 팀의 모집 역할들을 조회한다.")
//    @Test
//    void showTeamRoles(){
//
//        assertThat(team.getTeamRoles())
//                .extracting(TeamRole::getRole)
//                .containsExactlyElementsOf(roles);
//
//    }



}
