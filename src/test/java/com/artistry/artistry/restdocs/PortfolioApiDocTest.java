package com.artistry.artistry.restdocs;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.portfolio.PortfolioAccess;
import com.artistry.artistry.Dto.Request.ContentRequest;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class PortfolioApiDocTest extends ApiTest{
    private List<PortfolioResponse> createdPortfolios = new ArrayList<>();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation){
        super.setUp(restDocumentation);

        String title = "보컬 포트폴리오1";

        Role role1 = roleRepository.save(new Role("보컬"));
        Map<String, Object> body = getStringObjectMap(role1, title,"PUBLIC");

        PortfolioResponse response =
                given().body(body)
                        .when().post("/api/portfolios")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(PortfolioResponse.class);

        createdPortfolios.add(response);

        super.setUp(restDocumentation);

        String title2 = "보컬 포트폴리오1";

        Role role2 = roleRepository.save(new Role("보컬"));
        Map<String, Object> body2 = getStringObjectMap(role1, title,"PRIVATE");

        PortfolioResponse response2 =
                given().body(body2)
                        .when().post("/api/portfolios")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(PortfolioResponse.class);

        createdPortfolios.add(response2);
    }

    @DisplayName("전체 포트폴리오를 가져온다.")
    @Test
    void getAllPortfolios() {
        List<PortfolioResponse> responses =
                given()
                        .filter(RestAssuredRestDocumentationWrapper.document("read-portfolios",
                                "모든 포트폴리오 조회 API",
                                responseFields(
                                        fieldWithPath("[].id").description("포트폴리오 Id"),
                                        fieldWithPath("[].roleName").description("포트폴리오 역할"),
                                        fieldWithPath("[].title").description("포트폴리오 제목"),
                                        fieldWithPath("[].contents").description("포트폴리오 컨텐츠들"),
                                        fieldWithPath("[].contents[].url").description("컨텐츠 url"),
                                        fieldWithPath("[].contents[].comment").description("컨텐츠 설명"),
                                        fieldWithPath("[].access").description("포트폴리오 접근권한"))))
                        .when().get("/api/portfolios")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().jsonPath().getList(".", PortfolioResponse.class);

        assertThat(responses).isNotNull();
    }

    @DisplayName("Access로 포트폴리오 검색")
    @Test
    void getTagNames(){
        PortfolioAccess portfolioAccess = PortfolioAccess.PRIVATE;
        List<PortfolioResponse> responses =
                given()
                        .filter(RestAssuredRestDocumentationWrapper.document("read-Access-portfolios",
                                "Access 조회 API",
                                responseFields(
                                        fieldWithPath("[].id").description("포트폴리오 Id"),
                                        fieldWithPath("[].roleName").description("포트폴리오 역할"),
                                        fieldWithPath("[].title").description("포트폴리오 제목"),
                                        fieldWithPath("[].contents").description("포트폴리오 컨텐츠들"),
                                        fieldWithPath("[].contents[].url").description("컨텐츠 url"),
                                        fieldWithPath("[].contents[].comment").description("컨텐츠 설명"),
                                        fieldWithPath("[].access").description("포트폴리오 접근권한"))))
                        .when().get("/api/portfolios/access?access={portfolioAccess}",portfolioAccess)
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().jsonPath().getList(".", PortfolioResponse.class);

        assertThat(responses).hasSize(1);
    }
    
    @DisplayName("포트폴리오를 생성한다.")
    @Test
    void createPortfolio(){
        String title = "작곡가 포트폴리오1";

        Role role1 = roleRepository.save(new Role("작곡가"));
        Map<String, Object> body = getStringObjectMap(role1, title,"PUBLIC");

        PortfolioResponse response =
                given().body(body)
                        .filter(RestAssuredRestDocumentationWrapper.document("create-portfolio",
                                "포트폴리오 생성 API",
                                requestFields(
                                        fieldWithPath("title").description("포트폴리오 제목"),
                                        fieldWithPath("role").description("포트폴리오 역할"),
                                        fieldWithPath("role.id").description("역할 Id"),
                                        fieldWithPath("contents").description("포트폴리오 컨텐츠들"),
                                        fieldWithPath("contents[].url").description("컨텐츠 url"),
                                        fieldWithPath("contents[].comment").description("컨텐츠 설명"),
                                        fieldWithPath("access").description("포트폴리오 접근권한")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("포트폴리오 Id"),
                                        fieldWithPath("roleName").description("포트폴리오 역할"),
                                        fieldWithPath("title").description("포트폴리오 제목"),
                                        fieldWithPath("contents").description("포트폴리오 컨텐츠들"),
                                        fieldWithPath("contents[].url").description("컨텐츠 url"),
                                        fieldWithPath("contents[].comment").description("컨텐츠 설명"),
                                        fieldWithPath("access").description("포트폴리오 접근권한"))))
                        .when().post("/api/portfolios")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(PortfolioResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getRoleName()).isEqualTo(role1.getRoleName());
        //컨텐츠 테스트코드
        assertThat(response.getAccess()).isEqualTo("PUBLIC");
    }

    @DisplayName("포트폴리오를 수정한다.")
    @Test
    void updatePost() {
        Long idToUpdate = createdPortfolios.get(0).getId();

        String title = "수정된 포트폴리오";
        Role role1 = roleRepository.save(new Role("수정된 역할"));
        PortfolioAccess accessToChange = PortfolioAccess.PUBLIC;

        Map<String, Object> body = getStringObjectMap(role1, title,accessToChange.toString());
        body.put("id",idToUpdate);

        PortfolioResponse response = given().body(body)
                .filter(RestAssuredRestDocumentationWrapper.document("update-portfolio",
                        "포트폴리오 수정 API",
                        requestFields(
                                fieldWithPath("id").description("포트폴리오 Id"),
                                fieldWithPath("title").description("포트폴리오 제목"),
                                fieldWithPath("role").description("포트폴리오 역할"),
                                fieldWithPath("role.id").description("역할 Id"),
                                fieldWithPath("contents").description("포트폴리오 컨텐츠들"),
                                fieldWithPath("contents[].url").description("컨텐츠 url"),
                                fieldWithPath("contents[].comment").description("컨텐츠 설명"),
                                fieldWithPath("access").description("포트폴리오 접근권한")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포트폴리오 Id"),
                                fieldWithPath("roleName").description("포트폴리오 역할"),
                                fieldWithPath("title").description("포트폴리오 제목"),
                                fieldWithPath("contents").description("포트폴리오 컨텐츠들"),
                                fieldWithPath("contents[].url").description("컨텐츠 url"),
                                fieldWithPath("contents[].comment").description("컨텐츠 설명"),
                                fieldWithPath("access").description("포트폴리오 접근권한"))))
                .when().put("/api/portfolios")
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(PortfolioResponse.class);

        Assertions.assertThat(response.getId()).isEqualTo(idToUpdate);
        Assertions.assertThat(response.getTitle()).isEqualTo(title);
        Assertions.assertThat(response.getRoleName()).isEqualTo(role1.getRoleName());
        Assertions.assertThat(response.getAccess()).isEqualTo(accessToChange.toString());
    }


    @DisplayName("포트폴리오를 삭제한다.")
    @Test
    void deletePortfolio() {
        long idToDelete = createdPortfolios.get(0).getId();

        given().filter(RestAssuredRestDocumentationWrapper.document("delete-portfolio",
                        "포트폴리오 삭제 API"))
                .when().delete("/api/portfolios/{id}",idToDelete)
                .then().statusCode(HttpStatus.NO_CONTENT.value());

        int statusCode =  given()
                .when().get("/api/portfolios/{id}", idToDelete)
                .then().extract().statusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @NonNull
    private static Map<String, Object> getStringObjectMap(Role role1, String title,String access) {
        RoleRequest roleRequest = new RoleRequest(role1.getId()); // Create and populate RoleRequest object as needed
        List<ContentRequest> contents =
                List.of(new ContentRequest("https://www.youtube.com/watch?v=N9_hsXleJgs","fantasize"),
                        new ContentRequest("https://www.youtube.com/watch?v=RyZz1JX8xC8","victim")); // Create and populate ContentRequest list as needed

        Map<String,Object> body =new HashMap<>();
        body.put("title", title);
        body.put("role",roleRequest);
        body.put("contents",contents);
        body.put("access", access);
        return body;
    }
}
