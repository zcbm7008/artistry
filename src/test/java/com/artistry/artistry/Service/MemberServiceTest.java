package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Request.MemberInfoRequest;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.MemberTeamsResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MemberService memberService;

    @DisplayName("멤버를 저장한다.")
    @Test
    void createMemeber() {
        String expectedName = "new User";
        String expectedEmail = "newuser1@a.com";
        String expectedIconUrl = "a.url";

        MemberResponse response = memberService.create(new MemberCreateRequest(expectedName,expectedEmail,expectedIconUrl));

        assertThat(response.getId()).isNotNull();
        assertThat(response.getNickName()).isEqualTo(expectedName);
        assertThat(response.getEmail()).isEqualTo(expectedEmail);
        assertThat(response.getIconUrl()).isEqualTo(expectedIconUrl);

    }

    @DisplayName("deleted된 멤버를 검색할 때 예외를 던짐.")
    @Test
    void notFoundDeletedMember() {
        Member deletedMember = memberRepository.save(new Member("deleted","a2a@a.com","a.ulr"));
        memberService.delete(deletedMember);
        assertThatThrownBy(() -> memberService.findById(deletedMember.getId())).isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("Member Id가 없을경우 예외를 던짐.")
    @Test
    void memberNotFound(){
        assertThatThrownBy(() -> memberService.findEntityById(Long.MAX_VALUE))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("멤버가 참여한 팀 리스트를 출력한다.")
    @Test
    void getParticipatedTeams() {
        Member hostMember = memberRepository.save(new Member("host","host@a.com","1.url"));
        Member savedMember = memberRepository.save(new Member("nick1","savedmember@member.com","2.url"));
        Role role1 = roleRepository.save(new Role("작곡가"));
        Portfolio portfolio1 = portfolioRepository.save(new Portfolio("작곡가포폴1",role1));

        Team team1 = teamRepository.save(new Team("team1",hostMember, Arrays.asList(role1)));
        Team team2 = teamRepository.save(new Team("team2",savedMember, Arrays.asList(role1)));
        Application application = applicationRepository.save(new Application(team1,role1,savedMember,portfolio1));
        Application application2 = applicationRepository.save(new Application(team2,role1,savedMember,portfolio1));

        application.setStatus(ApplicationStatus.APPROVED);
        application2.setStatus(ApplicationStatus.APPROVED);

        MemberInfoRequest request = new MemberInfoRequest(savedMember.getEmail());
        MemberTeamsResponse response = memberService.getParticipatedTeams(request);

        assertThat(response.getTeamResponses()).hasSize(2);
        assertThat(response.getTeamResponses()).extracting(TeamResponse::getTeamId).containsExactlyInAnyOrder(team1.getId(), team2.getId());

    }
    
}
