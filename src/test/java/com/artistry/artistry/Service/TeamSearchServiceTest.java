package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional
public class TeamSearchServiceTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 20);
    @Autowired
    private TeamSearchService teamSearchService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MemberRepository memberRepository;

    String nameToFind;

    Tag tag1;
    Tag tag2;
    Role roleToFind;

    @BeforeEach
    void setUp() {
        Member host = memberRepository.save(new Member("searchhost", "searchhost@host.com", "searchhosturl"));
        Tag savedTag1 = tagRepository.save(new Tag("searchtag1"));
        Tag savedTag2 = tagRepository.save(new Tag("searchtag2"));
        Tag tag3 = tagRepository.save(new Tag("searchtag3"));
        Role role1 = roleRepository.save(new Role("searchrole1"));

        tag1 = tagRepository.findById(savedTag1.getId()).orElseThrow(() -> new NoSuchElementException("tag1 not found"));
        tag2 = tagRepository.findById(savedTag2.getId()).orElseThrow(() -> new NoSuchElementException("tag2 not found"));
        roleToFind = roleRepository.findById(role1.getId()).orElseThrow(() -> new NoSuchElementException("role not found"));

        nameToFind = "공모전";

        Team team1 =
                Team.builder()
                        .name(nameToFind + "참여하실분12312312")
                        .roles(List.of(role1))
                        .tags(Arrays.asList(tag1, tag2))
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        Team team2 =
                Team.builder()
                        .name(nameToFind + "참여하실분123343457")
                        .roles(List.of(role1))
                        .tags(Arrays.asList(tag1, tag2,tag3))
                        .host(host)
                        .teamStatus(TeamStatus.RECRUITING)
                        .build();

        teamRepository.save(team1);
        teamRepository.save(team2);
    }

    @DisplayName("title, roleIds, tagIds, status로 팀을 조회한다.")
    @Test
    void findTeamsQuery() {
        // Given
        TeamStatus statusToFind = TeamStatus.RECRUITING;
        List<Tag> tags = List.of(tag1, tag2);
        List<String> tagsToFind =
                tags.stream().map(Tag::getName).toList();

        List<Long> tagIdsToFind =
                tags.stream().map(Tag::getId).toList();

        TeamSearchRequest request = new TeamSearchRequest(nameToFind, List.of(roleToFind.getId()), tagIdsToFind, statusToFind);

        // When
        List<TeamResponse> responses = teamSearchService.searchTeams(request, PAGEABLE);

        // Then
        assertThat(responses).isNotEmpty();
        assertThat(responses).allMatch(teamResponse -> teamResponse.getName().contains(nameToFind));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getRoleNames().contains(roleToFind.getName()));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTeamStatus().equals(statusToFind.toString()));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTags().containsAll(tagsToFind));
    }

    @DisplayName("name, status로 팀을 조회한다.")
    @Test
    void findTeamsQueryByNameAndStatus() {
        // Given
        String name = "공모전";
        TeamStatus statusToFind = TeamStatus.RECRUITING;

        TeamSearchRequest request = new TeamSearchRequest(name, null, null, statusToFind);

        // When
        List<TeamResponse> responses = teamSearchService.searchTeams(request, PAGEABLE);

        // Then
        assertThat(responses).isNotEmpty();
        assertThat(responses).allMatch(teamResponse -> teamResponse.getName().contains(name));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTeamStatus().equals(statusToFind.toString()));
    }

    @DisplayName("roleIds, status로 팀을 조회한다.")
    @Test
    void testFindTeamsByRoleIdsAndStatus() {
        // Given
        TeamStatus statusToFind = TeamStatus.RECRUITING;

        TeamSearchRequest request = new TeamSearchRequest(null, List.of(roleToFind.getId()), null, statusToFind);

        // When
        List<TeamResponse> responses = teamSearchService.searchTeams(request, PAGEABLE);

        // Then
        assertThat(responses).isNotEmpty();
        assertThat(responses).allMatch(teamResponse -> teamResponse.getRoleNames().contains(roleToFind.getName()));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTeamStatus().equals(statusToFind.toString()));
    }

    @DisplayName("roleIds, tags, status로 팀을 조회한다.")
    @Test
    void testFindTeamsByRoleIdsTagsAndStatus() {
        // Given
        TeamStatus statusToFind = TeamStatus.RECRUITING;

        TeamSearchRequest request = new TeamSearchRequest(null, List.of(roleToFind.getId()), List.of(tag1.getId(), tag2.getId()), statusToFind);

        // When
        List<TeamResponse> responses = teamSearchService.searchTeams(request, PAGEABLE);

        // Then
        assertThat(responses).isNotEmpty();
        assertThat(responses).allMatch(teamResponse -> teamResponse.getRoleNames().contains(roleToFind.getName()));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTags().contains(tag1.getName()));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTags().contains(tag2.getName()));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTeamStatus().equals(statusToFind.toString()));
    }

    @DisplayName("name, tags로 팀을 조회한다.")
    @Test
    void testFindTeamsByNameAndTags() {
        // Given
        String name = "공모전";
        List<Tag> tags = List.of(tag1, tag2);

        List<Long> tagIdsToFind =
                tags.stream().map(Tag::getId).toList();

        TeamSearchRequest request = new TeamSearchRequest(name, null, tagIdsToFind, null);

        // When
        List<TeamResponse> responses = teamSearchService.searchTeams(request, PAGEABLE);

        // Then
        List<String> tagsToFind =
                tags.stream().map(Tag::getName).toList();

        assertThat(responses).isNotEmpty();
        assertThat(responses).allMatch(teamResponse -> teamResponse.getName().contains(name));
        assertThat(responses).allMatch(teamResponse -> teamResponse.getTags().containsAll(tagsToFind));
    }
}