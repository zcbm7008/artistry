package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Content;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.LinkResponse;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Exceptions.PortfolioNotFoundException;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.PortfolioRepository;
import com.artistry.artistry.Repository.RoleRepository;
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

import static com.artistry.artistry.Domain.portfolio.Portfolio.INIT_ACCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Transactional
@SpringBootTest
public class PortfolioServiceTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 100);

    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MemberRepository memberRepository;
    PortfolioResponse response;
    PortfolioResponse response2;
    PortfolioResponse response3;
    PortfolioResponse response4;
    Member savedMember;
    Role role;

    int PublicSize = 0;
    int PrivateSize = 0;

    @BeforeEach
    void setUp(){
        savedMember = memberRepository.save(new Member("m1", "m1@a.com", "a.url"));

        String title = "보컬 포트폴리오";

        role = roleRepository.save(new Role("보컬"));


        LinkRequest linkRequest1 = new LinkRequest("https://www.youtube.com/watch?v=FWW9YYTz5T8", "Anita");
        LinkRequest linkRequest2 = new LinkRequest("https://www.youtube.com/watch?v=YmNj2FvdHho", "Amphetamine");
        List<LinkRequest> contents = Arrays.asList(linkRequest1, linkRequest2);

        response = createPublicPortfolio(savedMember,title,role,contents);

        String title2 = "보컬 2";

        LinkRequest linkRequest3 = new LinkRequest("https://www.youtube.com/watch?v=FWW9YYTz5T8", "Anita");
        LinkRequest linkRequest4 = new LinkRequest("https://www.youtube.com/watch?v=YmNj2FvdHho", "Amphetamine");
        List<LinkRequest> contents2 = Arrays.asList(linkRequest3, linkRequest4);

        response2 = createPublicPortfolio(savedMember,title2,role,contents2);

        String title3 = "보컬 3";

        LinkRequest linkRequest5 = new LinkRequest("https://youtu.be/OvDFAmwu-FM?si=yn0RgYp69Mm9xmGD", "So High");
        List<LinkRequest> contents3 = Arrays.asList(linkRequest5);

        response3 = createPrivatePortfolio(savedMember,title3,role,contents3);
        response4 = createPrivatePortfolio(savedMember,title,role,contents3);

    }

    private PortfolioResponse createPublicPortfolio(Member member, String title, Role role, List<LinkRequest> contents){
        RoleRequest roleRequest = new RoleRequest(role.getId());
        PortfolioCreateRequest request = new PortfolioCreateRequest(member.getId(),title,roleRequest,contents,"PUBLIC");

        PublicSize+=1;

        return portfolioService.create(request);
    }

    private PortfolioResponse createPrivatePortfolio(Member member, String title, Role role, List<LinkRequest> contents){
        RoleRequest roleRequest = new RoleRequest(role.getId());
        PortfolioCreateRequest request = new PortfolioCreateRequest(member.getId(),title,roleRequest,contents,"PRIVATE");

        PrivateSize+=1;

        return portfolioService.create(request);
    }

    @DisplayName("portfolio Id가 없을경우 예외를 던짐.")
    @Test
    void PortfolioNotFound(){
        assertThatThrownBy(() -> portfolioService.findEntityById(Long.MAX_VALUE))
                .isInstanceOf(PortfolioNotFoundException.class);
    }

    @DisplayName("Access 값이 존재하지 않을 때 예외를 던짐")
    @Test
    void acessNotExists() {
        String title = "작곡가 포트폴리오";
        Role role = new Role("작곡가");
        Role savedRole = roleRepository.save(role);

        RoleRequest roleRequest = new RoleRequest(savedRole.getId());

        LinkRequest linkRequest1 = new LinkRequest("https://www.youtube.com/watch?v=lzQpS1rH3zI", "Fast");
        LinkRequest linkRequest2 = new LinkRequest("https://www.youtube.com/watch?v=9LSyWM2CL-U", "Empty");

        List<LinkRequest> contents = Arrays.asList(linkRequest1, linkRequest2);

        PortfolioCreateRequest request = new PortfolioCreateRequest(savedMember.getId(),title, roleRequest, contents, "imimimimi");
        //when

        assertThatThrownBy(() -> portfolioService.create(request))
                .isInstanceOf(IllegalArgumentException.class);

    }


    @DisplayName("portfolio를 생성한다")
    @Test
    void createPublicPortfolio() {
        //given
        String title = "작곡가 포트폴리오";
        Role role = new Role("작곡가");
        Role savedRole = roleRepository.save(role);

        RoleRequest roleRequest = new RoleRequest(savedRole.getId());

        LinkRequest linkRequest1 = new LinkRequest("https://www.youtube.com/watch?v=lzQpS1rH3zI", "Fast");
        LinkRequest linkRequest2 = new LinkRequest("https://www.youtube.com/watch?v=9LSyWM2CL-U", "Empty");

        List<LinkRequest> contents = Arrays.asList(linkRequest1, linkRequest2);

        PortfolioCreateRequest request = new PortfolioCreateRequest(savedMember.getId(),title, roleRequest, contents, "PUBLIC");
        //when
        PortfolioResponse response = portfolioService.create(request);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getRoleName()).isEqualTo(savedRole.getName());
        assertThat(response.getContents()).hasSize(2)
                .extracting(LinkResponse::getUrl, LinkResponse::getComment)
                .containsExactlyInAnyOrder(
                        tuple(linkRequest1.getUrl(), linkRequest1.getComment()),
                        tuple(linkRequest2.getUrl(), linkRequest2.getComment())
                );
    }

    @DisplayName("요청한 Id의 포트폴리오 정보를 반환한다.")
    @Test
    void findPortfolio(){
        String title = "작곡가 포트폴리오";
        String roleName = "작곡가";
        Role role = new Role(roleName);
        Role savedRole = roleRepository.save(role);

        Content content1 = new Content("https://www.youtube.com/watch?v=lzQpS1rH3zI", "Fast");
        Content content2 = new Content("https://www.youtube.com/watch?v=9LSyWM2CL-U", "Empty");

        List<Content> contents = Arrays.asList(content1, content2);

        Portfolio portfolio = new Portfolio(savedMember,title,role);
        portfolio.addContents(contents);

        portfolioRepository.save(portfolio);

        //when
        PortfolioResponse portfolioResponse = portfolioService.findPortfolioById(portfolio.getId());

        assertThat(portfolioResponse.getId()).isEqualTo(portfolio.getId());
        assertThat(portfolioResponse.getTitle()).isEqualTo(title);
        assertThat(portfolioResponse.getRoleName()).isEqualTo(roleName);

        assertThat(portfolioResponse.getContents()).hasSize(contents.size())
                .extracting(LinkResponse::getUrl)
                .containsExactlyInAnyOrder(content1.getUrl(), content2.getUrl());

        assertThat(portfolioResponse.getContents()).hasSize(contents.size())
                .extracting(LinkResponse::getUrl)
                .containsExactlyInAnyOrderElementsOf(contents.stream().map(Content::getUrl).collect(Collectors.toList()));

        assertThat(portfolioResponse.getContents()).hasSize(contents.size())
                .extracting(LinkResponse::getComment)
                .containsExactlyInAnyOrderElementsOf(contents.stream().map(Content::getComment).collect(Collectors.toList()));

        assertThat(portfolioResponse.getAccess()).isEqualTo(INIT_ACCESS.toString());

    }

    @DisplayName("조회를 하면 view가 증가한다.")
    @Test
    void increaseView(){
        portfolioService.findByIdAndIncreaseView(response.getId());
        portfolioService.findByIdAndIncreaseView(response.getId());
        portfolioService.findByIdAndIncreaseView(response.getId());
        PortfolioResponse response1 = portfolioService.findByIdAndIncreaseView(response.getId());

        assertThat(response1.getView()).isEqualTo(4L);
    }

    @DisplayName("좋아요를 하면 view가 증가한다.")
    @Test
    void increaseLike(){
        portfolioService.increaseLike(response.getId());
        portfolioService.increaseLike(response.getId());
        portfolioService.increaseLike(response.getId());
        PortfolioResponse response1 = portfolioService.increaseLike(response.getId());

        assertThat(response1.getLike()).isEqualTo(4L);
    }

    @DisplayName("모든 포트폴리오를 조회한다.")
    @Test
    void findPortfolios(){
        //when
        List<PortfolioResponse> portfolios = portfolioService.findAll(PAGEABLE);

        //then
        assertThat(portfolios).hasSize(PublicSize+PrivateSize);
    }

    @DisplayName("포트폴리오를 페이지네이션으로 조회한다.")
    @Test
    void findPagePortfolios(){
        //when
        List<PortfolioResponse> portfolios = portfolioService.findAll(PageRequest.of(0,3));

        //then
        assertThat(portfolios).hasSize(3);
    }

    @DisplayName("두번째 페이지의 첫번째 글을 조회한다.")
    @Test
    void find2PageFirstPortfolios(){
        //when
        List<PortfolioResponse> portfolios = portfolioService.findAll(PageRequest.of(1,2));

        PortfolioResponse portfolioResponse = portfolios.get(0);

        //then
        assertThat(portfolios).hasSize(2);
        assertThat(portfolioResponse.getTitle()).isEqualTo(response3.getTitle());
        assertThat(portfolioResponse.getMember().getId()).isEqualTo(response3.getMember().getId());

        assertThat(portfolioResponse.getContents())
                .extracting(LinkResponse::getUrl)
                .containsExactlyInAnyOrderElementsOf(response3.getContents().stream().map(LinkResponse::getUrl).collect(Collectors.toList()));

        assertThat(portfolioResponse.getContents())
                .extracting(LinkResponse::getComment)
                .containsExactlyInAnyOrderElementsOf(response3.getContents().stream().map(LinkResponse::getComment).collect(Collectors.toList()));

    }

    @DisplayName("모든 Public 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfolio(){
        //when
        List<PortfolioResponse> portfolios = portfolioService.findAllByAccess(PortfolioAccess.PUBLIC,PAGEABLE);

        //then
        assertThat(portfolios).hasSize(PublicSize);
        assertThat(portfolios).extracting(PortfolioResponse::getAccess).containsOnly(String.valueOf(PortfolioAccess.PUBLIC));
    }

    @DisplayName("모든 Private 포트폴리오를 조회한다.")
    @Test
    void findPrivatePortfolio(){
        //when
        List<PortfolioResponse> portfolios = portfolioService.findAllByAccess(PortfolioAccess.PRIVATE,PAGEABLE);

        //then
        assertThat(portfolios).hasSize(PrivateSize);
        assertThat(portfolios).extracting(PortfolioResponse::getAccess).containsOnly(String.valueOf(PortfolioAccess.PRIVATE));
    }

    @DisplayName("특정 member의 모든 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfoliosByMember(){
        MemberInfoRequest request = new MemberInfoRequest(savedMember.getId());
        List <PortfolioResponse> portfolioResponses = portfolioService.findAllPublicByMember(request,PAGEABLE);

        assertThat(portfolioResponses)
                .extracting(PortfolioResponse::getMember)
                .extracting(MemberResponse::getId)
                .containsOnly(savedMember.getId());
    }

    @DisplayName("특정 title로 모든 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfoliosByTitle(){
        String title = "보컬";
        List <PortfolioResponse> portfolioResponses = portfolioService.findAllPublicByTitle(title,PAGEABLE);

        assertThat(portfolioResponses).allMatch(portfolio -> portfolio.getTitle().contains(title));
    }
    @DisplayName("특정 Role의 모든 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfoliosByRole(){
        RoleRequest request = new RoleRequest(role.getId());
        List <PortfolioResponse> portfolioResponses = portfolioService.findAllPublicByRole(request,PAGEABLE);

        assertThat(portfolioResponses).allMatch(portfolio -> portfolio.getRoleName().equals(role.getName()));
    }

    @DisplayName("title, memberId, roleId로 포트폴리오를 조회한다.")
    @Test
    void findPortfoliosQuery(){
        //Given
        String title = "작곡가";

        PortfolioSearchRequest request = new PortfolioSearchRequest(title,savedMember.getId(),role.getId());
        PortfolioSearchRequest request2 = new PortfolioSearchRequest(title,null,null);
        //When
        List<PortfolioResponse> responses = portfolioService.searchPortfolios(request,PAGEABLE);

        //Then
        assertThat(responses).allMatch(portfolioResponse -> portfolioResponse.getTitle().contains(title));
        assertThat(responses).allMatch(portfolioResponse -> portfolioResponse.getMember().getId().equals(savedMember.getId()));
        assertThat(responses).allMatch(portfolioResponse -> portfolioResponse.getRoleName().equals(role.getName()));

        //When
        List<PortfolioResponse> responses2 = portfolioService.searchPortfolios(request2,PAGEABLE);
        assertThat(responses2).allMatch(portfolioResponse -> portfolioResponse.getTitle().contains(title));

    }


    @DisplayName("포트폴리오를 수정한다.")
    @Test
    void update() {
        Content content = new Content("https://www.youtube.com/watch?v=ABwmmg5UNNg", "I Deserve");

        LinkRequest updatedRequest = new LinkRequest(content.getUrl(), content.getComment());

        //given
        PortfolioResponse expected = new PortfolioResponse(response.getId(),
                "수정된 타이틀",
                response.getMember(),
                "수정된 역할",
                Arrays.asList(LinkResponse.from(content)),
                "PRIVATE",
                0L,
                0L);

        Role role1 = new Role("수정된 역할");
        roleRepository.save(role1);
        RoleRequest roleRequest = new RoleRequest(role1.getId());
        PortfolioUpdateRequest request = new PortfolioUpdateRequest(response.getId(),
                "수정된 타이틀",
                roleRequest,
                Arrays.asList(updatedRequest),
                "PRIVATE");

        //when
        PortfolioResponse response = portfolioService.update(request);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }
    
    @DisplayName("포트폴리오를 삭제한다.")
    @Test
    void delete() {

        //when
        portfolioService.delete(response.getId());

        //then
        List<PortfolioResponse> responses = portfolioService.findAll(PAGEABLE);
        assertThat(responses).hasSize(PublicSize+PrivateSize-1);
    }
}
