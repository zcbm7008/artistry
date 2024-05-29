package com.artistry.artistry.auth.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponse {
    private final String access_token;
    private final String refresh_token;
    private final String id_token;
    private final String expires_in;
    private final String token_type;
    private final String scope;
}
