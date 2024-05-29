package com.artistry.artistry.auth.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponse {
    private final String accessToken;
}
