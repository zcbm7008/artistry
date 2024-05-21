package com.artistry.artistry.Service;

import com.artistry.artistry.Exceptions.PortfolioNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PortfolioRepositoryTest {

    @Autowired
    private PortfolioService portfolioService;

    @DisplayName("porfolio Id가 없을경우 예외를 던짐.")
    @Test
    void PortfolioNotFound(){
        assertThatThrownBy(() -> portfolioService.findById(Long.MAX_VALUE))
                .isInstanceOf(PortfolioNotFoundException.class);
    }

}