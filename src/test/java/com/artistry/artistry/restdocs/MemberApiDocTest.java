package com.artistry.artistry.restdocs;

import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.Dto.Response.RoleResponse;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class MemberApiDocTest extends ApiTest{
    private List<MemberResponse> createdMembers = new ArrayList<>();

    @BeforeEach
    void setUp(){
       createdMembers.add(memberSave("member1"));
    }

    public static MemberResponse memberSave(final String memberName){
        Map<String, Object> body = new HashMap<>();
        body.put("nickName",memberName);
        body.put("email","test@t.com");
        body.put("iconUrl","test.url");

        return given().body(body)
                .when().post("/api/members")
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(MemberResponse.class);
    }

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
                                        fieldWithPath("iconUrl").description("멤버 아이콘 url"),
                                        fieldWithPath("email").description("멤버 이메일"))))
                        .when().post("/api/members")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(MemberResponse.class);
    }

    @DisplayName("멤버 정보를 수정한다.")
    @Test
    void updateMember(){
        MemberResponse member = createdMembers.get(0);
        String expectedNickname = "changedNickname";
        String expectedUrl = "changed.url";
        Map<String, Object> body = new HashMap<>();
        body.put("nickName", expectedNickname);
        body.put("iconUrl", expectedUrl);

        MemberResponse response =
                given().body(body)
                        .filter(RestAssuredRestDocumentationWrapper.document("update-member",
                                "멤버 수정 API",
                                requestFields(
                                        fieldWithPath("nickName").description("멤버 닉네임"),
                                        fieldWithPath("iconUrl").description("멤버 아이콘 url")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("멤버 Id"),
                                        fieldWithPath("nickName").description("멤버 이름"),
                                        fieldWithPath("iconUrl").description("멤버 아이콘 url"),
                                        fieldWithPath("email").description("멤버 이메일"))))
                        .when().put("/api/members/" + member.getId())
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(MemberResponse.class);

        assertThat(response.getId()).isEqualTo(member.getId());
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getNickName()).isEqualTo(expectedNickname);
        assertThat(response.getIconUrl()).isEqualTo(expectedUrl);

    }

}
