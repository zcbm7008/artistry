package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Content;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.ContentResponse;
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
    Member savedMember;
    Role role;




    @BeforeEach
    void setUp(){
        String title = "보컬 포트폴리오";
        role = new Role("보컬");
        Role savedRole = roleRepository.save(role);
        savedMember = memberRepository.save(new Member("m1","m1@a.com","a.url"));

        RoleRequest roleRequest = new RoleRequest(savedRole.getId());

        ContentRequest contentRequest1 = new ContentRequest("https://www.youtube.com/watch?v=FWW9YYTz5T8", "Anita");
        ContentRequest contentRequest2 = new ContentRequest("https://www.youtube.com/watch?v=YmNj2FvdHho", "Amphetamine");

        List<ContentRequest> contents = Arrays.asList(contentRequest1, contentRequest2);

        PortfolioCreateRequest request = new PortfolioCreateRequest(savedMember.getId(),title, roleRequest, contents, "PUBLIC");
        //when
        response = portfolioService.create(request);


        String title2 = "보컬 포트폴리오";
        Role role2 = new Role("보컬");
        Role savedRole2 = roleRepository.save(role2);

        RoleRequest roleRequest2 = new RoleRequest(savedRole2.getId());

        ContentRequest contentRequest3 = new ContentRequest("https://www.youtube.com/watch?v=FWW9YYTz5T8", "Anita");
        ContentRequest contentRequest4 = new ContentRequest("https://www.youtube.com/watch?v=YmNj2FvdHho", "Amphetamine");

        List<ContentRequest> contents2 = Arrays.asList(contentRequest3, contentRequest4);

        PortfolioCreateRequest request2 = new PortfolioCreateRequest(savedMember.getId(),title2, roleRequest2, contents2, "PRIVATE");
        //when
        portfolioService.create(request2);
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

        ContentRequest contentRequest1 = new ContentRequest("https://www.youtube.com/watch?v=lzQpS1rH3zI", "Fast");
        ContentRequest contentRequest2 = new ContentRequest("https://www.youtube.com/watch?v=9LSyWM2CL-U", "Empty");

        List<ContentRequest> contents = Arrays.asList(contentRequest1, contentRequest2);

        PortfolioCreateRequest request = new PortfolioCreateRequest(savedMember.getId(),title, roleRequest, contents, "imimimimi");
        //when

        assertThatThrownBy(() -> portfolioService.create(request))
                .isInstanceOf(IllegalArgumentException.class);

    }


    @DisplayName("portfolio를 생성한다")
    @Test
    void createPortfolio() {
        //given
        String title = "작곡가 포트폴리오";
        Role role = new Role("작곡가");
        Role savedRole = roleRepository.save(role);

        RoleRequest roleRequest = new RoleRequest(savedRole.getId());

        ContentRequest contentRequest1 = new ContentRequest("https://www.youtube.com/watch?v=lzQpS1rH3zI", "Fast");
        ContentRequest contentRequest2 = new ContentRequest("https://www.youtube.com/watch?v=9LSyWM2CL-U", "Empty");

        List<ContentRequest> contents = Arrays.asList(contentRequest1, contentRequest2);

        PortfolioCreateRequest request = new PortfolioCreateRequest(savedMember.getId(),title, roleRequest, contents, "PUBLIC");
        //when
        PortfolioResponse response = portfolioService.create(request);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getRoleName()).isEqualTo(savedRole.getName());
        assertThat(response.getContents()).hasSize(2)
                .extracting(ContentResponse::getUrl, ContentResponse::getComment)
                .containsExactlyInAnyOrder(
                        tuple(contentRequest1.getUrl(), contentRequest1.getComment()),
                        tuple(contentRequest2.getUrl(), contentRequest2.getComment())
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
                .extracting(ContentResponse::getUrl)
                .containsExactlyInAnyOrder(content1.getUrl(),content2.getUrl());

        assertThat(portfolioResponse.getContents()).hasSize(contents.size())
                .extracting(ContentResponse::getUrl)
                .containsExactlyInAnyOrderElementsOf(contents.stream().map(Content::getUrl).collect(Collectors.toList()));

        assertThat(portfolioResponse.getContents()).hasSize(contents.size())
                .extracting(ContentResponse::getComment)
                .containsExactlyInAnyOrderElementsOf(contents.stream().map(Content::getComment).collect(Collectors.toList()));

        assertThat(portfolioResponse.getAccess()).isEqualTo(INIT_ACCESS.toString());

    }

    @DisplayName("모든 포트폴리오를 조회한다.")
    @Test
    void findPortfolios(){
        //when
        List<PortfolioResponse> portfolios = portfolioService.findAll();
        //then
        assertThat(portfolios).hasSize(2);
    }

    @DisplayName("모든 Public 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfolio(){
        //when
        List<PortfolioResponse> portfolios = portfolioService.findAllByAccess(PortfolioAccess.PUBLIC);

        //then
        assertThat(portfolios).hasSize(1);
        assertThat(portfolios).extracting(PortfolioResponse::getAccess).containsExactly(String.valueOf(PortfolioAccess.PUBLIC));
    }

    @DisplayName("모든 Private 포트폴리오를 조회한다.")
    @Test
    void findPrivatePortfolio(){
        //when
        List<PortfolioResponse> portfolios = portfolioService.findAllByAccess(PortfolioAccess.PRIVATE);

        //then
        assertThat(portfolios).hasSize(1);
        assertThat(portfolios).extracting(PortfolioResponse::getAccess).containsExactly(String.valueOf(PortfolioAccess.PRIVATE));
    }

    @DisplayName("특정 member의 모든 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfoliosByMember(){
        MemberInfoRequest request = new MemberInfoRequest(savedMember.getId());
        List <PortfolioResponse> portfolioResponses = portfolioService.findAllPublicByMember(request);

        assertThat(portfolioResponses)
                .extracting(PortfolioResponse::getMember)
                .extracting(MemberResponse::getId)
                .containsExactly(savedMember.getId());
    }

    @DisplayName("특정 title로 모든 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfoliosByTitle(){
        String title = "보컬";
        List <PortfolioResponse> portfolioResponses = portfolioService.findAllPublicByTitle(title);

        assertThat(portfolioResponses).allMatch(portfolio -> portfolio.getTitle().contains(title));
    }
    @DisplayName("특정 Role의 모든 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfoliosByRole(){
        RoleRequest request = new RoleRequest(role.getId());
        List <PortfolioResponse> portfolioResponses = portfolioService.findAllPublicByRole(request);

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
        List<PortfolioResponse> responses = portfolioService.searchPublicPortfolios(request);

        //Then
        assertThat(responses).allMatch(portfolioResponse -> portfolioResponse.getTitle().contains(title));
        assertThat(responses).allMatch(portfolioResponse -> portfolioResponse.getMember().getId().equals(savedMember.getId()));
        assertThat(responses).allMatch(portfolioResponse -> portfolioResponse.getRoleName().equals(role.getName()));

        //When
        List<PortfolioResponse> responses2 = portfolioService.searchPublicPortfolios(request2);
        assertThat(responses2).allMatch(portfolioResponse -> portfolioResponse.getTitle().contains(title));

    }


    @DisplayName("포트폴리오를 수정한다.")
    @Test
    void update() {
        Content content = new Content("https://www.youtube.com/watch?v=ABwmmg5UNNg", "I Deserve");

        ContentRequest updatedRequest = new ContentRequest(content.getUrl(),content.getComment());

        //given
        PortfolioResponse expected = new PortfolioResponse(response.getId(),
                "수정된 타이틀",
                response.getMember(),
                "수정된 역할",
                Arrays.asList(ContentResponse.from(content)),
                "PRIVATE");

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
        List<PortfolioResponse> responses = portfolioService.findAll();
        assertThat(responses).hasSize(1);
    }
}
