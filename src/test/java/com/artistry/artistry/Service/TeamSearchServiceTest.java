package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamRole;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Exceptions.TeamNotRecruitingException;
import com.artistry.artistry.Exceptions.TeamRoleHasApprovedException;
import com.artistry.artistry.Exceptions.TeamRoleNotFoundException;
import com.artistry.artistry.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class TeamSearchServiceTest {

    private static final Pageable PAGEABLE = PageRequest.of(0, 100);

    @Autowired
    private TeamSearchService teamSearchService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    MemberRepository memberRepository;
    String nameToFind;
    Role roleToFind;
    Tag tag1;
    Tag tag2;

    @BeforeEach
    void setUp(){
        Member host = memberRepository.save(new Member("host","host@host.com","hosturl"));
        tag1 = tagRepository.save(new Tag("tag1"));
        tag2 = tagRepository.save(new Tag("tag2"));
        Tag tag3 = tagRepository.save(new Tag("tag3"));
        roleToFind = roleRepository.save(new Role("role1"));

        List<Tag> tagList = Arrays.asList(tag1,tag2);
        nameToFind = "공모전";

        Team team1 =
                Team.builder()
                        .name(nameToFind + "참여하실분12312312")
                        .roles(List.of(roleToFind))
                        .tags(tagList)
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        Team team2 =
                Team.builder()
                        .name(nameToFind + "참여하실분1233434")
                        .roles(List.of(roleToFind))
                        .tags(Arrays.asList(tag1,tag2,tag3))
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        teamRepository.save(team1);
        teamRepository.save(team2);
    }

    @DisplayName("title, roleIds, tagIds ,status로 팀을 조회한다.")
    @Test
    void findTeamsQuery(){
        //Given
        String name = "trap";
        TeamStatus statusToFind = TeamStatus.RECRUITING;
        List<Tag> tags = List.of(tag1,tag2);
        List<String> tagsToFind =
                tags.stream().map(Tag::getName).toList();

        List<Long> tagIdsToFind =
                tags.stream().map(Tag::getId).toList();


        TeamSearchRequest request = new TeamSearchRequest(name,List.of(roleToFind.getId()),tagIdsToFind,statusToFind);
        TeamSearchRequest request2 = new TeamSearchRequest(name,List.of(roleToFind.getId()),null,null);
        //When
        List<TeamResponse> responses = teamSearchService.searchTeams(request,PAGEABLE);
        List<TeamResponse> responses2 = teamSearchService.searchTeams(request2,PAGEABLE);


        //Then
        assertThat(responses).allMatch(teamResponse -> teamResponse.getName().contains(name));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getRoleNames().contains(roleToFind.getName()));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTeamStatus().equals(statusToFind.toString()));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTags().contains(tagsToFind));

        assertThat(responses2).allMatch(teamResponse -> teamResponse.getName().contains(name));

    }
}