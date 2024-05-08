package com.artistry.artistry.domain;

import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Repository.TeamRepository;
import com.artistry.artistry.Service.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TeamTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @DisplayName("팀 더미데이터 확인")
    @ParameterizedTest
    @CsvSource({"1,Group1","2,Group2"})
    void dummyGroupTest(Long id, String groupName){
        Optional<Team> team = teamRepository.findById(id);
        assertThat(team.isPresent()).isTrue();
        assertThat(team.get().getName()).isEqualTo(groupName);
    }

    @DisplayName("팀 Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> teamService.findById(Long.MAX_VALUE))
                .isInstanceOf(TeamNotFoundException.class);
    }
}
