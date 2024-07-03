package com.artistry.artistry.restdocs;

import com.artistry.artistry.Dto.Response.RoleResponse;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class RoleApiDocTest extends ApiTest{
    private List<RoleResponse> createdRoles = new ArrayList<>();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation){
        super.setUp(restDocumentation);

        createdRoles.add(roleSave("작곡가"));
        createdRoles.add(roleSave("보컬"));
        createdRoles.add(roleSave("일러스트레이터"));
        createdRoles.add(roleSave("드럼"));

    }

    public static RoleResponse roleSave(final String roleName){
        Map<String, Object> body = new HashMap<>();
        body.put("name",roleName);

        return given().body(body)
                .when().post("/api/roles")
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(RoleResponse.class);
    }

    @DisplayName("전체 역할을 가져온다.")
    @Test
    void getAllRolesTest() {

        List<RoleResponse> responses =
                given()
                        .filter(RestAssuredRestDocumentationWrapper.document("read-roles",
                        "모든 역할 조회 API",
                        responseFields(
                                fieldWithPath("[].id").description("역할 Id"),
                                fieldWithPath("[].name").description("역할 이름"))))
                        .when().get("/api/roles")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().jsonPath().getList(".", RoleResponse.class);

        assertThat(responses).isNotEmpty();
    }

    @DisplayName("역할 단일 조회")
    @Test
    void getRole() {
        Long tagId = createdRoles.get(0).getId();

        RoleResponse response =
                given()
                        .filter(RestAssuredRestDocumentationWrapper.document("create-tag",
                                "태그 단일 조회 API",
                                responseFields(
                                        fieldWithPath("id").description("역할 Id"),
                                        fieldWithPath("name").description("역할 이름"))))
                        .when().get("/api/roles/{id}", tagId)
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(RoleResponse.class);

        RoleResponse expectedRole = createdRoles.get(0);
        assertThat(response.getName()).isEqualTo(expectedRole.getName());
    }

    @DisplayName("역할을 생성한다.")
    @Test
    void createRoles(){
        String roleName = "프로듀서";

        Map<String,Object> body =new HashMap<>();
        body.put("name",roleName);

        RoleResponse response =
                given().body(body)
                    .filter(RestAssuredRestDocumentationWrapper.document("create-roles",
                            "역할 생성 API",
                requestFields(
                        fieldWithPath("name").description("역할 이름")
                ),
                responseFields(
                        fieldWithPath("id").description("역할 Id"),
                        fieldWithPath("name").description("역할 이름"))))
                        .when().post("/api/roles")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(RoleResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(roleName);

    }

    @DisplayName("역할을 수정한다.")
    @Test
    void updateRole(){

        String updateTagName = "보컬";
        Map<String,Object> body = new HashMap<>();
        body.put("name",updateTagName);
        Long idToUpdate = createdRoles.get(3).getId();

        RoleResponse response = given().body(body)
                .filter(RestAssuredRestDocumentationWrapper.document("update-roles",
                        "역할 업데이트 API",
                        requestFields(
                                fieldWithPath("name").description("역할 이름")
                        ),
                        responseFields(
                                fieldWithPath("id").description("역할 ID"),
                                fieldWithPath("name").description("역할 이름"))))
                .when().put("/api/roles/{id}", idToUpdate)
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(RoleResponse.class);


        assertThat(response.getId()).isEqualTo(idToUpdate);
        assertThat(response.getName()).isEqualTo(updateTagName);
    }

    @DisplayName("역할을 삭제한다")
    @Test
    void deleteRole(){
        long idToDelete = createdRoles.get(0).getId();

        given().filter(RestAssuredRestDocumentationWrapper.document("delete-role",
                "역할 삭제 API"))
                .when().delete("/api/roles/{id}",idToDelete)
                .then().statusCode(HttpStatus.NO_CONTENT.value());



        int statusCode =  given()
                .when().get("/api/roles/{id}", idToDelete)
                .then().extract().statusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
