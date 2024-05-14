package com.artistry.artistry.Domain;

import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        team.participate(applicant);

        //Then
        assertThat(team.getMembers()).hasSize(1);
        assertThat(team.getMembers()).contains(applicant);

    }

    @DisplayName("같은 유저의 요청이 이미 있을경우 예외가 발생한다.")
    @Test
    void DuplicatedMemberException(){
        //Given
        Team team = createTeam();
        Member applicant = Member.builder()
                .nickname("지원자1").build();

        //When
        team.participate(applicant);

        //Then
        assertThatThrownBy(() -> team.participate(applicant)).isInstanceOf(ArtistryDuplicatedException.class);

    }

    @DisplayName("한 포지션에 같은 멤버의 포트폴리오가 여러개면 중복처리한다.")
    @Test
    void DuplicatedRolePortfolioException(){
        //Given
        Role role1 = Role.builder().roleName("작곡가").build();

        List<Portfolio> portfolios = new ArrayList<>();

        Member applicant = Member.builder()
                .nickname("지원자1").portfolios(portfolios).build();

        Portfolio portfolio1 = Portfolio.builder()
                .role(role1).member(applicant).title("portfolio1").build();


        Portfolio portfolio2 = Portfolio.builder()
                .role(role1).member(applicant).title("portfolio2").build();

        applicant.addPortfolio(portfolio1);
        applicant.addPortfolio(portfolio2);

        Team team = createTeam();

        //when
        team.apply(applicant.getPortfolios().get(0));

        //then
        assertThatThrownBy(() -> team.apply(applicant.getPortfolios().get(1))).isInstanceOf(ArtistryDuplicatedException.class);
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
