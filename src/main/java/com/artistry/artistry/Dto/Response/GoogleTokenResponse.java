package com.artistry.artistry.Dto.Response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleTokenResponse {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String client_id;
}
