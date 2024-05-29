package com.artistry.artistry.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleTokenResponse {

    private String access_token;
    private String refresh_token;
    private String id_token;
    private String expires_in;
    private String token_type;
    private String scope;
}
