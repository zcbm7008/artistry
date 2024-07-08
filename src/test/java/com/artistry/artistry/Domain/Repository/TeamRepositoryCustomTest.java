package com.artistry.artistry.Domain.Repository;


import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamRole;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.Query.TeamQueryRepository;
import com.artistry.artistry.Repository.Query.TeamQueryRepositoryCustom;
import com.artistry.artistry.Repository.Query.TeamQueryRepositoryCustomImpl;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class TeamRepositoryCustomTest {

    @Autowired
    private TeamQueryRepositoryCustom teamQueryRepositoryCustom;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        Role role1 = roleRepository.save(new Role("작곡가"));
        Tag tag1 = tagRepository.save(new Tag("태그1"));
        Tag tag2 = tagRepository.save(new Tag("태그2"));

        Member member = memberRepository.save(new Member("hoster","a@a.com","a.url"));

        Team team1 =
                Team.builder()
                        .name("artists")
                        .roles(List.of(role1))
                        .tags(List.of(tag1,tag2))
                        .host(member)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        Team team2 =
                Team.builder()
                        .name("artists2")
                        .roles(List.of(role1))
                        .tags(List.of(tag1,tag2))
                        .host(member)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        teamRepository.save(team1);
        teamRepository.save(team2);

    }

    @Test
    @Transactional
    public void testSearchTeamsWithCriteria() {
        String name = "artist";
        TeamStatus status = TeamStatus.RECRUITING;
        Pageable pageable = PageRequest.of(0, 10);

        Slice<TeamResponse> result = teamQueryRepositoryCustom.searchTeamsWithCriteria(name, null, null, status, pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).contains("artist");
    }

}
