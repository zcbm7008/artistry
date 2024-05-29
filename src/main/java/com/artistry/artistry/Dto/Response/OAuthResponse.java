package com.artistry.artistry.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthResponse {
    private String jwtToken;
    private long user_id;
    private String accessToken;
    private String tokenType;
}
