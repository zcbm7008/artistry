package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.portfolio.ContentsType;
import com.artistry.artistry.Domain.portfolio.Portfolio;
import com.artistry.artistry.Domain.portfolio.PortfolioContent;
import com.artistry.artistry.Exceptions.PortfolioNotFoundException;
import com.artistry.artistry.Repository.PortfolioRepository;
import com.artistry.artistry.Repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PortfolioRepositoryTest {
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp(){

    }
    @DisplayName("포트폴리오를 생성한다.")
    @Test
    void savePortfolio(){
        //given
        Role role = new Role("작곡가");
        roleRepository.save(role);
        PortfolioContent portfolioContent1 = new PortfolioContent("https://youtu.be/TZlQp15pzPo?si=CauplleoztnFOI2x","buttons!", ContentsType.AUDIO);
        PortfolioContent portfolioContent2 = new PortfolioContent("https://youtu.be/iUGtYgMMZ0U?si=rBwck1vUEYQajvsm","dashstar*", ContentsType.AUDIO);
        Portfolio portfolio = new Portfolio("작곡가 포트폴리오1",role);

        portfolio.addContents(portfolioContent1);
        portfolio.addContents(portfolioContent2);

        //when
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        assertThat(savedPortfolio.getId()).isNotNull();
        assertThat(savedPortfolio.getTitle()).isEqualTo(portfolio.getTitle());
        assertThat(savedPortfolio.getRole().getRoleName()).isEqualTo(role.getRoleName());
        assertThat(savedPortfolio.getContents()).hasSize(2).contains(portfolioContent1,portfolioContent2);
    }

    @DisplayName("포트폴리오를 조회한다.")
    @Test
    void findPortfolio(){
        Role role = new Role("작곡가");
        roleRepository.save(role);
        PortfolioContent portfolioContent1 = new PortfolioContent("https://youtu.be/TZlQp15pzPo?si=CauplleoztnFOI2x","buttons!", ContentsType.AUDIO);
        PortfolioContent portfolioContent2 = new PortfolioContent("https://youtu.be/iUGtYgMMZ0U?si=rBwck1vUEYQajvsm","dashstar*", ContentsType.AUDIO);
        Portfolio createdportfolio = new Portfolio("작곡가 포트폴리오1",role);

        createdportfolio.addContents(portfolioContent1);
        createdportfolio.addContents(portfolioContent2);

        Portfolio savedPortfolio = portfolioRepository.save(createdportfolio);
        //when
        Portfolio portfolio = portfolioRepository.findById(savedPortfolio.getId()).orElseThrow(PortfolioNotFoundException::new);
        //then
        assertThat(portfolio).usingRecursiveComparison().isEqualTo(createdportfolio);
    }


}
