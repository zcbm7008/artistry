package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.Portfolio;
import com.artistry.artistry.Repository.PortfolioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PortfolioRepositoryTest {
    @Autowired
    private PortfolioRepository portfolioRepository;

//    @DisplayName("포트폴리오 더미 데이터를 확인한다")
//    @ParameterizedTest
//    @CsvSource({"1,Portfolio1","2,Portfolio2"})
//    void dummyPortfolioTest(Long id, String title){
//        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
//        assertThat(portfolio.isPresent()).isTrue();
//        assertThat(portfolio.get().getTitle()).isEqualTo(title);
//
//    }


}
