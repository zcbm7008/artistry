package com.artistry.artistry.restdocs;

import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Domain.Team;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Repository.MemberRepository;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
import com.artistry.artistry.Repository.TeamRepository;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class TeamApiDocTest {
    @LocalServerPort
    private int port;


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    MemberRepository memberRepository;
    private Team dummyTeam;
    TeamResponse teamResponse1;

    private RequestSpecification specification;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        specification = new RequestSpecBuilder()
                .addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

        Role role1 = roleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 1"));
        Role role2 = roleRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 2"));

        Tag tag1 = tagRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 1"));
        Tag tag2 = tagRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 2"));

        Member member1 = memberRepository.save(new Member("member1"));

        // 더미 팀 생성
        String dummyTeamName = "더미 팀";
        List<Role> roles = Arrays.asList(role1, role2);
        List<Tag> tags = Arrays.asList(tag1, tag2);
        dummyTeam = new Team(0L,dummyTeamName, member1, tags, roles);
        teamRepository.save(dummyTeam);

        teamResponse1 = create_team(dummyTeamName,member1.getId(),roles,tags);



    }

    public static TeamResponse create_team(String teamName,Long hostId, List<Role> roles,List<Tag> tags){
        Map<String, Object> body = new HashMap<>();
        body.put("teamName", teamName);
        body.put("hostId",hostId);
        body.put("roles", roles.stream().map(role -> Map.of("id",role.getId(),"roleName",role.getRoleName())).collect(Collectors.toList()));
        body.put("tags", roles.stream().map(tag -> Map.of("id",tag.getId(),"roleName",tag.getRoleName())).collect(Collectors.toList()));

        return   given().log().all()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/api/teams")
                .then().statusCode(201)
                .extract().body().as(TeamResponse.class);
    }


    @DisplayName("팀을 생성한다")
    @Test
    void createTeamTest() throws Exception{

        Role role1 = roleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 1"));
        Role role2 = roleRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 2"));

        Tag tag1 = tagRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 1"));
        Tag tag2 = tagRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 2"));


        Map<String, Object> body = new HashMap<>();
        body.put("teamName", "팀1");
        body.put("hostId",1L);
        body.put("roles", Arrays.asList(Map.of("id", role1.getId(), "roleName", role1.getRoleName()),
                Map.of("id", role2.getId(), "roleName", role2.getRoleName())));
        body.put("tags", Arrays.asList(Map.of("id", tag1.getId(), "name", tag1.getName()),
                Map.of("id", tag2.getId(), "name", tag2.getName())));


        given(specification).log().all()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .filter(RestAssuredRestDocumentationWrapper.document("create-team",
                        "팀 생성 API",
                        requestFields(fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("hostId").description("호스트 Id"),
                                fieldWithPath("tags").description("태그 리스트"),
                                fieldWithPath("roles").description("역할 리스트"),
                                fieldWithPath("roles[].id").description("역할 Id"),
                                fieldWithPath("roles[].roleName").description("역할 이름"),
                                fieldWithPath("tags[].id").description("태그 Id"),
                                fieldWithPath("tags[].name").description("태그 리스트")),
                        responseFields(fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("createdAt").description("팀 생성 시각"),
                                fieldWithPath("host.id").description("호스트 Id"),
                                fieldWithPath("host.nickName").description("호스트 닉네임"),
                                fieldWithPath("teamRoles").description("팀 역할 리스트"),
                                fieldWithPath("teamRoles[].id").description("팀 역할 Id"),
                                fieldWithPath("teamRoles[].teamId").ignored(),
                                fieldWithPath("teamRoles[].role").ignored(),
                                fieldWithPath("teamRoles[].role.id").ignored(),
                                fieldWithPath("teamRoles[].role.roleName").description("역할 이름"),
                                fieldWithPath("teamRoles[].applications").description("팀 역할에 지원한 지원서"),
                                fieldWithPath("tags").description("태그 리스트"))))
                .when().post("/api/teams")
                .then().statusCode(201)
                .extract().body().as(TeamResponse.class);
    }




    
    @DisplayName("팀을 조회한다")
    @Test
    void readTeamTest() throws Exception{

        TeamResponse teamResponse =
                RestAssured.given(specification).log().all()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .filter(RestAssuredRestDocumentationWrapper.document("read-team",
                        "팀 조회 API",
                        responseFields(fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("createdAt").description("팀 생성 시각"),
                                fieldWithPath("host.id").description("호스트 Id"),
                                fieldWithPath("host.nickName").description("호스트 닉네임"),
                                fieldWithPath("tags").description("팀 태그"),
                                fieldWithPath("teamRoles").description("팀 역할"),
                                fieldWithPath("teamRoles[].id").description("팀 역할 Id"),
                                fieldWithPath("teamRoles[].teamId").ignored(),
                                fieldWithPath("teamRoles[].role").ignored(),
                                fieldWithPath("teamRoles[].role.id").ignored(),
                                fieldWithPath("teamRoles[].role.roleName").description("역할 이름"),
                                fieldWithPath("teamRoles[].applications").description("팀 역할에 지원한 지원서"))))
                .when().get("/api/teams/{id}",teamResponse1.getTeamId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(TeamResponse.class);
    }
}
