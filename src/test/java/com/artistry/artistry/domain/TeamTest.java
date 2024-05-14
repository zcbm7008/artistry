package com.artistry.artistry.domain;

import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Exceptions.MemberDuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TeamTest {

    @DisplayName("팀에 유저가 요청을 보낸다.")
    @Test
    void requestJoinTeam(){
        //Given
        Team team = createTeam();
        Member applicant = Member.builder().nickname("지원자1").build();

        //When
        team.apply(applicant);

        //Then
        assertThat(team.getApplicants()).hasSize(1);
        assertThat(team.getApplicants()).contains(applicant);

    }

    @DisplayName("같은 유저의 요청이 이미 있을경우 예외가 발생한다.")
    @Test
    void DuplicatedMemberException(){
        //Given
        Team team = createTeam();
        Member applicant = Member.builder().nickname("지원자1").build();

        //When
        team.apply(applicant);

        //Then
        assertThatThrownBy(() -> team.apply(applicant)).isInstanceOf(MemberDuplicatedException.class);

    }

    private Team createTeam(){
        List<Tag> tags= Arrays.asList(
                Tag.builder().name("밴드").build(),
                Tag.builder().name("얼터너티브 락").build()
        );

        List<Role> roles = Arrays.asList(
                Role.builder().roleName("작곡가").build(),
                Role.builder().roleName("보컬").build()
        );

        Member host = Member.builder()
                .nickname("호스트")
                .build();

        return Team.builder()
                .name("밴드")
                .tags(tags)
                .host(host)
                .roles(roles)
                .build();
    }
}
