package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.Application;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.LinkRequest;
import com.artistry.artistry.Dto.Request.MemberCreateRequest;
import com.artistry.artistry.Dto.Request.MemberInfoRequest;
import com.artistry.artistry.Dto.Request.MemberUpdateRequest;
import com.artistry.artistry.Dto.Response.LinkResponse;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.MemberTeamsResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.ArtistryDuplicatedException;
import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.PortfolioRepository;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TeamRepository;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class MemberServiceTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 100);

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MemberService memberService;

    String duplicatedName;
    String duplicatedEmail;

    MemberResponse response;

    @BeforeEach
    void setUp(){
        duplicatedName = "thisismyname";
        duplicatedEmail = "duplicate@dup.com";

        response = memberService.create(new MemberCreateRequest(duplicatedName,duplicatedEmail,"expectedIconUrl"));

    }

    @DisplayName("멤버를 저장한다.")
    @Test
    void createMemeber() {
        //Given
        String expectedName = "new User";
        String expectedEmail = "newuser1@a.com";
        String expectedIconUrl = "a.url";

        //When
        MemberResponse response = memberService.create(new MemberCreateRequest(expectedName,expectedEmail,expectedIconUrl));

        //Then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getNickName()).isEqualTo(expectedName);
        assertThat(response.getEmail()).isEqualTo(expectedEmail);
        assertThat(response.getIconUrl()).isEqualTo(expectedIconUrl);
    }

    @DisplayName("nickname으로 member를 검색한다.")
    @Test
    void findByNickName(){
        memberRepository.save(new Member("superhotfire","a@a.com","url"));
        memberRepository.save(new Member("superhotfire22","a@a.com","url"));
        memberRepository.save(new Member("superhotfire33","a@a.com","url"));

        memberRepository.save(new Member("rapname32","a@a.com","url"));
        memberRepository.save(new Member("rapname322","a@a.com","url"));

        String nicknameToFind = "superhotfire";

        String nicknameToFind2 = "rapname";

        List<MemberResponse> responses = memberService.findAllByNickName(nicknameToFind,PAGEABLE);
        List<MemberResponse> responses2 = memberService.findAllByNickName(nicknameToFind2,PAGEABLE);

        assertThat(responses).hasSize(3).
                allMatch(memberResponse -> memberResponse.getNickName().contains(nicknameToFind));

        assertThat(responses2).hasSize(2)
                .allMatch(memberResponse -> memberResponse.getNickName().contains(nicknameToFind2));

    }

    @DisplayName("중복된 Email로 생성할 때 예외를 던짐.")
    @Test
    void duplicatedEmailException(){
        assertThatThrownBy(() -> memberService.create(new MemberCreateRequest("fddfddfd",duplicatedEmail,"a.url")))
                .isInstanceOf(ArtistryDuplicatedException.class);
    }

    @DisplayName("멤버 정보를 수정한다.")
    @Test
    void updateMember(){
        //Given
        String updatedName = "updatedName";
        String updatedUrl = "update.url";
        String bio = "this is my bio";
        LinkRequest linkReq1 = new LinkRequest("twitterurl","twitter");
        LinkRequest linkReq2 = new LinkRequest("instagramurl","instagram");

        List<LinkRequest> linkRequests = List.of(linkReq1,linkReq2);
        MemberUpdateRequest request = new MemberUpdateRequest(updatedName,updatedUrl,bio,linkRequests);

        //When
        memberService.update(response.getId(),request);
        MemberResponse updatedResponse = memberService.findById(response.getId());

        //Then
        assertThat(updatedResponse.getNickName()).isEqualTo(updatedName);
        assertThat(updatedResponse.getIconUrl()).isEqualTo(updatedUrl);
        assertThat(updatedResponse.getBio()).isEqualTo(bio);

        assertThat(updatedResponse.getLinks())
                .extracting(LinkResponse::getUrl)
                .containsExactlyElementsOf(linkRequests.stream().map(LinkRequest::getUrl).collect(Collectors.toList()));

        assertThat(updatedResponse.getLinks())
                .extracting(LinkResponse::getComment)
                .containsExactlyElementsOf(linkRequests.stream().map(LinkRequest::getComment).collect(Collectors.toList()));
    }

    @DisplayName("deleted된 멤버를 검색할 때 예외를 던짐.")
    @Test
    void notFoundDeletedMember() {
        //Given
        Member deletedMember = memberRepository.save(new Member("deleted","a2@a.com","a.ulr"));

        //When
        memberService.delete(deletedMember);

        //Then
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
        //Given
        Member hostMember = memberRepository.save(new Member("host","host@a.com","1.url"));
        Member savedMember = memberRepository.save(new Member("nick1","savedmember@member.com","2.url"));
        Role role1 = roleRepository.save(new Role("작곡가"));
        Portfolio portfolio1 = portfolioRepository.save(new Portfolio(savedMember,"작곡가포폴1",role1));

        Team team1 = teamRepository.save(new Team("team1",hostMember, Arrays.asList(role1)));
        Team team2 = teamRepository.save(new Team("team2",savedMember, Arrays.asList(role1)));
        Application application1 = team1.apply(portfolio1);
        Application application2 = team2.apply(portfolio1);

        application1.setStatus(ApplicationStatus.APPROVED);
        application2.setStatus(ApplicationStatus.APPROVED);

        MemberInfoRequest request = new MemberInfoRequest(savedMember.getId());

        //When
        MemberTeamsResponse response = memberService.getParticipatedTeams(request);

        //Then
        assertThat(response.getTeamResponses()).hasSize(2);
        assertThat(response.getTeamResponses()).extracting(TeamResponse::getId).containsExactlyInAnyOrder(team1.getId(), team2.getId());

    }
    
}
