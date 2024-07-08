package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Repository.Query.TeamQueryRepository;
import com.artistry.artistry.Repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@Transactional
@DataJpaTest
public class TeamQueryRepositoryTest {
        @Autowired
        private TeamRepository teamRepository;
        @Autowired
        private TeamQueryRepository teamQueryRepository;

        @Test
        public void testSearchTeamIdsWithCriteria() {
            // Given
            // Create and save some test data
            Team team1 = new Team();
            team1.setName("Team A");
            team1.setTeamStatus(TeamStatus.RECRUITING);
            // Add roles and tags to team1 as needed
            teamRepository.save(team1);

            Team team2 = new Team();
            team2.setName("Team B");
            team2.setTeamStatus(TeamStatus.FINISHED);
            // Add roles and tags to team2 as needed
            teamRepository.save(team2);

            // When
            Pageable pageable = PageRequest.of(0, 10);
            Slice<Long> result = teamQueryRepository.searchTeamIdsWithCriteria(
                    "Team A", null, null, TeamStatus.RECRUITING, pageable);

            // Then
            List<Long> teamIds = result.getContent();
            assertThat(teamIds).isNotEmpty();
            assertThat(teamIds).containsExactly(team1.getId());
        }
}
