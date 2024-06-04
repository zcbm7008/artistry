package com.artistry.artistry.restdocs;


import com.artistry.artistry.Service.OAuthService;
import com.artistry.artistry.auth.oauth.SocialType;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthApiDocTest extends ApiTest{
    private final OAuthService oAuthService;

    @Autowired
    public AuthApiDocTest(OAuthService oAuthService){
        this.oAuthService = oAuthService;
    }

    @DisplayName("로그인 URL을 생성한다.")
    @Test
    void getLoginUrl() {
        String provider = "google";
        String expectedUrl = "http://example.com/login";

        // Sending the request and validating the response
        Response response = given()
                .pathParam("provider", provider)
                .when()
                .get("api/auth/oauth/{provider}/login")
                .then()
                .statusCode(HttpStatus.FOUND.value())
                .extract()
                .response();

        // Verifying the Location header
        String locationHeader = response.getHeader("Location");
        assert locationHeader != null;
        assert locationHeader.equals(expectedUrl);
    }
}
