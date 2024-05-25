package com.artistry.artistry.Dto.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioUpdateRequest {
    @NotNull
    private Long id;
    @NotEmpty(message = "포트폴리오 타이틀은 공백일 수 없습니다.")
    private String title;
    private RoleRequest role;
    private List<ContentRequest> contents;
    @NotEmpty(message = "공개범위를 설정해야 합니다.")
    private String access;

}
