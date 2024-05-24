package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.portfolio.Portfolio;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioRequest {
    @NotEmpty(message = "포트폴리오 타이틀은 공백일 수 없습니다.")
    private String title;
    private RoleRequest role;
    private List<ContentRequest> contents;

}
