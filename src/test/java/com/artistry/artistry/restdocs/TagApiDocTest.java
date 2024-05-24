package com.artistry.artistry.restdocs;


import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Dto.Request.TagCreateRequest;
import com.artistry.artistry.Dto.Request.TagRequest;
import com.artistry.artistry.Dto.Response.TagNameResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class TagApiDocTest extends ApiTest{
    private List<TagResponse> createTags = new ArrayList<>();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation){
        super.setUp(restDocumentation);

        createTags.add(tagSave("트랩"));
        createTags.add(tagSave("퓨처베이스"));
        createTags.add(tagSave("퓨처리딤"));

    }

    public static TagResponse tagSave(final String tagName){
        Map<String, Object> body = new HashMap<>();
        body.put("name",tagName);

        return given().body(body)
                .when().post("/api/tags")
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(TagResponse.class);
    }

    @DisplayName("전체 태그를 가져온다.")
    @Test
    void getAllTagsTest() {

        List<TagResponse> responses =
                given()
                        .filter(RestAssuredRestDocumentationWrapper.document("read-tags",
                        "모든 태그 조회 API",
                        responseFields(
                                fieldWithPath("[].id").description("태그 Id"),
                                fieldWithPath("[].name").description("태그 이름"))))
                        .when().get("/api/tags")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().jsonPath().getList(".", TagResponse.class);

        assertThat(responses).isNotEmpty();
    }

    @DisplayName("태그 단일 조회")
    @Test
    void getTag() {
        Long tagId = createTags.get(0).getId();

        TagResponse response =
                given()
                        .filter(RestAssuredRestDocumentationWrapper.document("create-tag",
                                "태그 단일 조회 API",
                                responseFields(
                                        fieldWithPath("id").description("태그 Id"),
                                        fieldWithPath("name").description("태그 이름"))))
                        .when().get("/api/tags/{id}", tagId)
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(TagResponse.class);

        TagResponse expectedTag = createTags.get(0);
        assertThat(response.getName()).isEqualTo(expectedTag.getName());
    }

    @DisplayName("태그 이름 검색")
    @Test
    void getTagNames(){
        String name = "퓨처";

        List <TagNameResponse> responses = given().
                filter(RestAssuredRestDocumentationWrapper.document("read-tags-by-name",
                "태그 이름 조회 API",
                responseFields(
                        fieldWithPath("[].id").description("태그 Id"),
                        fieldWithPath("[].name").description("태그 이름"))))
                .when().get("/api/tags/name?name={name}", name)
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", TagNameResponse.class);

        List<String> foundNames = responses.stream()
                .map(TagNameResponse::getName)
                .toList();

        assertThat(foundNames).hasSize(2)
                .contains("퓨처베이스","퓨처리딤");

    }

    @DisplayName("태그를 생성한다.")
    @Test
    void createTag(){
        String tagName = "퓨처베이스";

        Map<String,Object> body =new HashMap<>();
        body.put("name",tagName);

        TagResponse response =
                given().body(body)
                    .filter(RestAssuredRestDocumentationWrapper.document("create-tag",
                            "태그 생성 API",
                requestFields(
                        fieldWithPath("name").description("태그 Id")
                ),
                responseFields(
                        fieldWithPath("id").description("태그 Id"),
                        fieldWithPath("name").description("태그 이름"))))
                        .when().post("/api/tags")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(TagResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(tagName);

    }
}
