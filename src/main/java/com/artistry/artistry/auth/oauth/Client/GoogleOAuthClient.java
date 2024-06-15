package com.artistry.artistry.auth.oauth.Client;

import com.artistry.artistry.Dto.Request.OAuthMemberRequest;
import com.artistry.artistry.Dto.Response.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class GoogleOAuthClient extends AbstractOAuthClient{
    private static final String JWT_DELIMITER = "\\.";
    private static final int PAYLOAD_INDEX = 1;


    public GoogleOAuthClient(@Value("${oauth.google.redirect_uri}") final String redirectUri,
                             @Value("${oauth.google.client_id}") final String clientId,
                             @Value("${oauth.google.client_secret}") final String clientSecret,
                             @Value("${oauth.google.token_uri}") final String tokenUri,
                             final RestTemplate restTemplate, final ObjectMapper objectMapper) {
        super(redirectUri,clientId,clientSecret,tokenUri,restTemplate,objectMapper);
    }

    @Override
    public OAuthMemberRequest createOAuthMemberRequest(final TokenResponse tokenResponse){
        final String idToken = tokenResponse.getId_token();
        final String name = extractElementFromToken(idToken,"name");
        final String email = extractElementFromToken(idToken,"email");
        final String picture = extractElementFromToken(idToken, "picture");

        return new OAuthMemberRequest(name,email,picture);
    }

    private String extractElementFromToken(final String idToken, final String key) {
        final String payLoad = idToken.split("\\.")[PAYLOAD_INDEX];
        final String decodedPayLoad = new String(Base64.getDecoder().decode(payLoad));
        final JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        return (String) jacksonJsonParser.parseMap(decodedPayLoad)
                .get(key);
    }
}
