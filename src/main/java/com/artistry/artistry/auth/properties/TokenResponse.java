package com.artistry.artistry.auth.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
