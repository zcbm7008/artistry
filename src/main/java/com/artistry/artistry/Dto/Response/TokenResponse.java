package com.artistry.artistry.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenResponse {
    private String access_token;
    private String refresh_token;
    private String id_token;
    private String expires_in;
    private String token_type;
    private String scope;
}
