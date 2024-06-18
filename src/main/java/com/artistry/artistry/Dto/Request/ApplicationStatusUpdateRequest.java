package com.artistry.artistry.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusUpdateRequest {
    private ApplicationInfoRequest application;
    private Long memberId;
    private String status;

}