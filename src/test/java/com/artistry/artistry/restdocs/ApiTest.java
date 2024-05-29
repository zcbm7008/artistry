package com.artistry.artistry.restdocs;


import com.artistry.artistry.DataLoader;
import com.artistry.artistry.Repository.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApiTest {

    protected static RequestSpecification specification;

    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    protected TagRepository tagRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected TeamRepository teamRepository;

    @BeforeEach
    protected void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        specification = new RequestSpecBuilder()
                .addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    protected static RequestSpecification given(){
        return  RestAssured.given(specification).log().all()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }
    @AfterEach
    void teardown() throws Exception {
        clearDatabase();
        initializeDatabase();
    }

    private void clearDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        List<String> tableNames = jdbcTemplate.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'", String.class);
        for (String tableName : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
    private void initializeDatabase() throws Exception {
        DataLoader dataLoader = new DataLoader(tagRepository,memberRepository,roleRepository,portfolioRepository);
        dataLoader.run();
    }

}
