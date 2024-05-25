package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.portfolio.ContentsType;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.Content;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Exceptions.PortfolioNotFoundException;
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

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class PortfolioRepositoryTest {
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private RoleRepository roleRepository;

    Portfolio savedPublicPortfolio;
    Portfolio savedPrivatePortfolio;
    Portfolio createdportfolio;
    Portfolio createdportfolio2;

    @BeforeEach
    public void setUp(){
        Role role = new Role("작곡가");
        roleRepository.save(role);
        Content content1 = new Content("https://youtu.be/TZlQp15pzPo?si=CauplleoztnFOI2x","buttons!", ContentsType.AUDIO);
        Content content2 = new Content("https://youtu.be/iUGtYgMMZ0U?si=rBwck1vUEYQajvsm","dashstar*", ContentsType.AUDIO);

        createdportfolio = new Portfolio("작곡가 포트폴리오1",role);

        createdportfolio.addContents(Arrays.asList(content1,content2));
        createdportfolio.setPortfolioAccess(PortfolioAccess.PUBLIC);
        savedPublicPortfolio = portfolioRepository.save(createdportfolio);

        Role role2 = new Role("보컬");
        roleRepository.save(role2);
        Content content3 = new Content("https://www.youtube.com/watch?v=tU0vm-dG-H0","Maraca", ContentsType.AUDIO);
        Content content4 = new Content("https://www.youtube.com/watch?v=OsGR31e0kjw","L.M.F", ContentsType.AUDIO);

        createdportfolio2 = new Portfolio("작곡가 포트폴리오1",role);

        createdportfolio2.addContents(Arrays.asList(content3,content4));
        createdportfolio2.setPortfolioAccess(PortfolioAccess.PRIVATE);
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
        Portfolio portfolio = new Portfolio("작곡가 포트폴리오1",role);

        portfolio.addContents(Arrays.asList(content1,content2));


        //when
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        assertThat(savedPortfolio.getId()).isNotNull();
        assertThat(savedPortfolio.getTitle()).isEqualTo(portfolio.getTitle());
        assertThat(savedPortfolio.getRole().getRoleName()).isEqualTo(role.getRoleName());
        assertThat(savedPortfolio.getContents()).hasSize(2).contains(content1, content2);
        assertThat(savedPortfolio.getPortfolioAccess()).isEqualTo(PortfolioAccess.PRIVATE);
    }

    @DisplayName("모든 포트폴리오를 조회한다.")
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
        List<Portfolio> portfolios = portfolioRepository.findByPortfolioAccess(PortfolioAccess.PUBLIC);

        //then
        assertThat(portfolios).hasSize(1);
        assertThat(portfolios).extracting(Portfolio::getPortfolioAccess).containsExactly(PortfolioAccess.PUBLIC);
    }

    @DisplayName("모든 Private 포트폴리오를 조회한다.")
    @Test
    void findPrivatePortfolio(){
        //when
        List<Portfolio> portfolios = portfolioRepository.findByPortfolioAccess(PortfolioAccess.PRIVATE);

        //then
        assertThat(portfolios).hasSize(1);
        assertThat(portfolios).extracting(Portfolio::getPortfolioAccess).containsExactly(PortfolioAccess.PRIVATE);
    }



}
