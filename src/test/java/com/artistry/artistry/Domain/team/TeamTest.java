package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Exceptions.TeamNotRecruitingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class TeamTest {
    Member host;
    Member member1;

    @BeforeEach
    void setUp(){
        host = new Member("host","host@host.com","hosturl");
        member1 = new Member("member1","member@member.com","memberurl");
    }

    @DisplayName("팀의 host를 확인한다.")
    @Test
    void isHost(){
        Team team =
                Team.builder()
                        .name("testteam")
                        .roles(List.of(new Role("role1")))
                        .host(host).build();

        assertThat(team.isHostMember(host)).isTrue();
    }

    @DisplayName("팀이 recruiting중이 아니면 지원할 때 예외를 출력한다.")
    @Test
    void notRecruiting(){
        Role role = new Role("role1");
        Team team=
                Team.builder()
                        .name("testteam")
                        .roles(List.of(role))
                        .host(host)
                        .teamStatus(TeamStatus.CANCELED).build();

        Portfolio portfolio = new Portfolio(member1,"title1",role);

        assertThatThrownBy(() -> team.apply(portfolio)).isInstanceOf(TeamNotRecruitingException.class);

    }

}
