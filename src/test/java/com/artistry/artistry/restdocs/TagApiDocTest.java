package com.artistry.artistry.restdocs;


import com.artistry.artistry.DataLoader;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Repository.*;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
