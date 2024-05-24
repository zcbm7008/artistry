package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Dto.Request.ContentRequest;
import com.artistry.artistry.Dto.Request.PortfolioRequest;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Response.ContentResponse;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Response.RoleResponse;
import com.artistry.artistry.Exceptions.PortfolioNotFoundException;
import com.artistry.artistry.Repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private RoleRepository roleRepository;

    @DisplayName("porfolio Id가 없을경우 예외를 던짐.")
    @Test
    void PortfolioNotFound(){
        assertThatThrownBy(() -> portfolioService.findById(Long.MAX_VALUE))
                .isInstanceOf(PortfolioNotFoundException.class);
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

        PortfolioRequest request = new PortfolioRequest(title, roleRequest, contents);
        //when
        PortfolioResponse response = portfolioService.create(request);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getRoleName()).isEqualTo(savedRole.getRoleName());
        assertThat(response.getContents()).hasSize(2)
                .extracting(ContentResponse::getUrl, ContentResponse::getComment)
                .containsExactlyInAnyOrder(
                        tuple(contentRequest1.getUrl(), contentRequest1.getComment()),
                        tuple(contentRequest2.getUrl(), contentRequest2.getComment())
                );
    }
}
