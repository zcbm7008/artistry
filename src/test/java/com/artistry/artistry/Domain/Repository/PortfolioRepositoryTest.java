package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.common.ContentsType;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.portfolio.Content;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
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
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.artistry.artistry.Domain.portfolio.Portfolio.INIT_ACCESS;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class PortfolioRepositoryTest {
    private static final Pageable PAGEABLE = PageRequest.of(0, 100);

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    MemberRepository memberRepository;

    Portfolio savedPublicPortfolio;
    Portfolio savedPrivatePortfolio;
    Portfolio createdportfolio;
    Portfolio createdportfolio2;
    Member member;
    Role role;

    @BeforeEach
    public void setUp(){
        role = new Role("작곡가");
        member = memberRepository.save(new Member("a","a2@a.com","aurl"));
        roleRepository.save(role);
        Content content1 = new Content("https://youtu.be/TZlQp15pzPo?si=CauplleoztnFOI2x","buttons!", ContentsType.AUDIO);
        Content content2 = new Content("https://youtu.be/iUGtYgMMZ0U?si=rBwck1vUEYQajvsm","dashstar*", ContentsType.AUDIO);

        createdportfolio = new Portfolio(member,"작곡가 포트폴리오1",role);

        createdportfolio.addContents(Arrays.asList(content1, content2));
        createdportfolio.setAccess(PortfolioAccess.PUBLIC);
        savedPublicPortfolio = portfolioRepository.save(createdportfolio);

        Role role2 = new Role("보컬");
        roleRepository.save(role2);
        Content content3 = new Content("https://www.youtube.com/watch?v=tU0vm-dG-H0","Maraca", ContentsType.AUDIO);
        Content content4 = new Content("https://www.youtube.com/watch?v=OsGR31e0kjw","L.M.F", ContentsType.AUDIO);

        createdportfolio2 = new Portfolio(member,"작곡가 포트폴리오3",role);

        createdportfolio2.addContents(Arrays.asList(content3, content4));
        createdportfolio2.setAccess(PortfolioAccess.PRIVATE);
        savedPrivatePortfolio = portfolioRepository.save(createdportfolio2);

    }
    @DisplayName("포트폴리오를 생성한다.")
    @Test
    void savePortfolio(){
        //given
        Role role = new Role("작곡가");
        roleRepository.save(role);
        Content content1 = new Content("https://youtu.be/TZlQp15pzPo?si=CauplleoztnFOI2x","buttons!", ContentsType.AUDIO);
        Content content2 = new Content("https://youtu.be/iUGtYgMMZ0U?si=rBwck1vUEYQajvsm","dashstar*", ContentsType.AUDIO);
        Portfolio portfolio = new Portfolio(member,"작곡가 포트폴리오1",role);

        portfolio.addContents(Arrays.asList(content1, content2));


        //when
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        assertThat(savedPortfolio.getId()).isNotNull();
        assertThat(savedPortfolio.getTitle()).isEqualTo(portfolio.getTitle());
        assertThat(savedPortfolio.getRole().getName()).isEqualTo(role.getName());
        assertThat(savedPortfolio.getContents()).hasSize(2).contains(content1, content2);
        assertThat(savedPortfolio.getAccess()).isEqualTo(INIT_ACCESS);
    }

    @DisplayName("포트폴리오를 Id로 조회한다.")
    @Test
    void findPortfolio(){

        //when
        Portfolio portfolio = portfolioRepository.findById(savedPublicPortfolio.getId()).orElseThrow(PortfolioNotFoundException::new);
        //then
        assertThat(portfolio).usingRecursiveComparison().isEqualTo(createdportfolio);
    }

    @DisplayName("모든 Public 포트폴리오를 조회한다.")
    @Test
    void findPublicPortfolio(){
        //when
        Slice<Portfolio> portfolios = portfolioRepository.findByAccess(PortfolioAccess.PUBLIC,PAGEABLE);

        //then
        assertThat(portfolios).hasSize(1);
        assertThat(portfolios).extracting(Portfolio::getAccess).containsExactly(PortfolioAccess.PUBLIC);
    }

    @DisplayName("모든 Private 포트폴리오를 조회한다.")
    @Test
    void findPrivatePortfolio(){
        //when
        Slice<Portfolio> portfolios = portfolioRepository.findByAccess(PortfolioAccess.PRIVATE,PAGEABLE);

        //then
        assertThat(portfolios).hasSize(1);
        assertThat(portfolios).extracting(Portfolio::getAccess).containsExactly(PortfolioAccess.PRIVATE);
    }

    @DisplayName("특정 멤버로 특정 엑세스의 포트폴리오를 조회한다.")
    @Test
    void findPortfoliosByMember(){
        Slice<Portfolio> publicPortfolios = portfolioRepository.findByMemberAndAccess(member,PortfolioAccess.PUBLIC,PAGEABLE);

        assertThat(publicPortfolios).hasSize(1)
                .allMatch(portfolio -> portfolio.getMember().getId().equals(member.getId()));

        Slice<Portfolio> privatePortfolios = portfolioRepository.findByMemberAndAccess(member,PortfolioAccess.PRIVATE,PAGEABLE);

        assertThat(privatePortfolios).hasSize(1)
                .allMatch(portfolio -> portfolio.getMember().getId().equals(member.getId()));
    }

    @DisplayName("title이 포함된 특정 엑세스의 포트폴리오를 조회한다.")
    @Test
    void findPortfoliosByTest(){
        String title = "작곡가";
        Slice<Portfolio> publicPortfolios = portfolioRepository.findByTitleContainingAndAccess(title,PortfolioAccess.PUBLIC,PAGEABLE);

        assertThat(publicPortfolios).hasSize(1)
                .allMatch(portfolio -> portfolio.getTitle().contains(title));
    }

    @DisplayName("role로 특정 엑세스의 포트폴리오를 조회한다.")
    @Test
    void findPortfoliosByRole(){
        String title = "작곡가";
        Slice<Portfolio> publicPortfolios = portfolioRepository.findByRoleAndAccess(role,PortfolioAccess.PUBLIC,PAGEABLE);

        assertThat(publicPortfolios).hasSize(1)
                .allMatch(portfolio -> portfolio.getTitle().contains(title));
    }

}
