package com.artistry.artistry.Domain.team;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TeamTest {

    @DisplayName("팀의 host를 확인한다.")
    @Test
    void isHost(){
        Member host = new Member("host","host@host.com","hosturl");
        Team team =
                Team.builder()
                        .name("testteam")
                        .roles(List.of(new Role("role1")))
                        .host(host).build();

        assertThat(team.isHostMember(host)).isTrue();
    }
}
