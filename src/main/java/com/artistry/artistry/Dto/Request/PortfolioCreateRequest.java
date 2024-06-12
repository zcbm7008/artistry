package com.artistry.artistry.Dto.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioCreateRequest {
    @NotEmpty(message = "포트폴리오 타이틀은 공백일 수 없습니다.")
    private Long memberId;
    private String title;
    private RoleRequest role;
    private List<ContentRequest> contents;
    private String access;

}
