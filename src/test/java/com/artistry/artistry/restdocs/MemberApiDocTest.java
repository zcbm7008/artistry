package com.artistry.artistry.restdocs;

import com.artistry.artistry.Dto.Response.MemberResponse;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class MemberApiDocTest extends ApiTest{

    @DisplayName("멤버를 생성한다.")
    @Test
    void createMember() {
        Map<String, Object> body = new HashMap<>();
        body.put("nickName", "nickname1");
        body.put("email", "a@a.com");
        body.put("iconUrl", "asdsd.url");

        MemberResponse response =
                given().body(body)
                        .filter(RestAssuredRestDocumentationWrapper.document("create-member",
                                "멤버 생성 API",
                                requestFields(
                                        fieldWithPath("nickName").description("멤버 닉네임"),
                                        fieldWithPath("email").description("멤버 이메일"),
                                        fieldWithPath("iconUrl").description("멤버 아이콘 url")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("멤버 Id"),
                                        fieldWithPath("nickName").description("멤버 이름"),
                                        fieldWithPath("iconUrl").description("멤버 아이콘 url"))))
                        .when().post("/api/members")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(MemberResponse.class);
    }

}
