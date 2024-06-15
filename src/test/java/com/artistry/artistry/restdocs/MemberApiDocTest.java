package com.artistry.artistry.restdocs;


import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.member.MemberLink;
import com.artistry.artistry.Dto.Request.LinkRequest;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
import com.artistry.artistry.Dto.Response.LinkResponse;
import com.artistry.artistry.Dto.Response.MemberResponse;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class MemberApiDocTest extends ApiTest{
    private final JwtTokenProvider jwtTokenProvider;
    private final List<MemberResponse> createdMembers = new ArrayList<>();
    private final Map<String, Object> parameters = new HashMap<>();


    @Autowired
    public MemberApiDocTest(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }
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


    @DisplayName("엑세스 토큰을 사용해 자신의 정보를 확인할 수 있다.")
    @Test
    void checkProfile(){
        String email = createdMembers.get(0).getEmail();

        AccessTokenResponse accessTokenResponse = given().when().get("/api/auth/fake/tokens?email=" + email)
                .then().log().all()
                .extract()
                .as(AccessTokenResponse.class);

        String accessToken = accessTokenResponse.getAccessToken();
        parameters.put(email,accessToken);

        MemberResponse member = createdMembers.get(0);
        String accesstoken = (String) parameters.get(email);

        MemberResponse response = given().log().all().header(HttpHeaders.AUTHORIZATION, accesstoken)
                .when()
                .get("/api/members/me")
                .then().log().all().extract()
                .as(MemberResponse.class);

        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getNickName()).isEqualTo(member.getNickName());
        assertThat(response.getIconUrl()).isEqualTo(member.getIconUrl());
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
                                        fieldWithPath("email").description("멤버 이메일"),
                                        fieldWithPath("links").description("멤버 링크"))))
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

        List <LinkRequest> links = List.of(new LinkRequest("twitterurl","twitter"),new LinkRequest("tiktokurl","tiktok"));

        Map<String, Object> body = new HashMap<>();

        body.put("nickName", expectedNickname);
        body.put("iconUrl", expectedUrl);
        body.put("links", links.stream().
                map(link -> Map.of("url",link.getUrl(),"comment",link.getComment()))
                .collect(Collectors.toList()));

        MemberResponse response =
                given().body(body)
                        .filter(RestAssuredRestDocumentationWrapper.document("update-member",
                                "멤버 수정 API",
                                requestFields(
                                        fieldWithPath("nickName").description("멤버 닉네임"),
                                        fieldWithPath("iconUrl").description("멤버 아이콘 url"),
                                        fieldWithPath("links").description("멤버 링크"),
                                        fieldWithPath("links[].url").description("멤버 링크 url"),
                                        fieldWithPath("links[].comment").description("멤버 링크 코멘트")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("멤버 Id"),
                                        fieldWithPath("nickName").description("멤버 이름"),
                                        fieldWithPath("iconUrl").description("멤버 아이콘 url"),
                                        fieldWithPath("email").description("멤버 이메일"),
                                        fieldWithPath("links").description("멤버 링크"),
                                        fieldWithPath("links[].url").description("멤버 링크 url"),
                                        fieldWithPath("links[].comment").description("멤버 링크 코멘트"))))
                        .when().put("/api/members/" + member.getId())
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(MemberResponse.class);

        assertThat(response.getId()).isEqualTo(member.getId());
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getNickName()).isEqualTo(expectedNickname);
        assertThat(response.getIconUrl()).isEqualTo(expectedUrl);

    }

    @DisplayName("멤버를 삭제한다.")
    @Test
    void deleteMember(){
        long idToDelete = createdMembers.get(0).getId();

        given().filter(RestAssuredRestDocumentationWrapper.document("delete-member",
                        "멤버 삭제 API"))
                .when().delete("/api/members/{id}",idToDelete)
                .then().statusCode(HttpStatus.NO_CONTENT.value());



        int statusCode =  given()
                .when().get("/api/tags/{id}", idToDelete)
                .then().extract().statusCode();

        AssertionsForClassTypes.assertThat(statusCode).isEqualTo(HttpStatus.NOT_FOUND.value());
    }



}
