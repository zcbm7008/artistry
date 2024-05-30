package com.artistry.artistry.restdocs;

import com.artistry.artistry.Dto.Response.LoginUrlResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class AuthApiDocTest extends ApiTest{

    @DisplayName("로그인 URL을 생성한다.")
    @Test
    void getLoginUrl() {
        String provider = "naver";
        LoginUrlResponse response =
                given().filter(RestAssuredRestDocumentationWrapper.document("create-login-url",
                                "로그인 url API",
                                pathParameters(
                                        parameterWithName("provider").description("OAuth 플랫폼")
                                ),
                                responseFields(
                                        fieldWithPath("url").description("로그인 URL"))))
                        .when().get("/api/auth/oauth/{provider}/login", provider)
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(LoginUrlResponse.class);

        assertThat(response.getUrl()).isNotNull();
    }
}
