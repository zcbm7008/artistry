package com.artistry.artistry.restdocs;


import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class TagApiDocTest extends ApiTest{


    @DisplayName("전체 태그를 가져온다.")
    @Test
    void getAllTagsTest() {
        tagRepository.save(new Tag("태그1"));
        tagRepository.save(new Tag("태그2"));

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
}
