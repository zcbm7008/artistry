package com.artistry.artistry.restdocs;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class TeamApiDocTest extends ApiTest{

    private Team dummyTeam;
    TeamResponse teamResponse1;
    @BeforeEach
    public void setUpData() {
        roleRepository.save(new Role("작곡가"));
        roleRepository.save(new Role("일러스트레이터"));
        roleRepository.save(new Role("작사가"));
        roleRepository.save(new Role("영상편집"));
        Role role1 = roleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 1"));
        Role role2 = roleRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 2"));

        tagRepository.save(new Tag("힙합"));
        tagRepository.save(new Tag("퓨처"));
        tagRepository.save(new Tag("하이퍼팝"));
        tagRepository.save(new Tag("뭄바톤"));
        Tag tag1 = tagRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 1"));
        Tag tag2 = tagRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 2"));

        Member member1 = memberRepository.save(new Member("member1","a@a.com"));

        // 더미 팀 생성
        String dummyTeamName = "더미 팀";
        List<Role> roles = Arrays.asList(role1, role2);
        List<Tag> tags = Arrays.asList(tag1, tag2);
        dummyTeam = new Team(dummyTeamName, member1, tags, roles);
        teamRepository.save(dummyTeam);

        teamResponse1 = create_team(dummyTeamName,member1.getId(),roles,tags);
    }

    public static TeamResponse create_team(String teamName,Long hostId, List<Role> roles,List<Tag> tags){
        Map<String, Object> body = new HashMap<>();
        body.put("teamName", teamName);
        body.put("hostId",hostId);
        body.put("roles", roles.stream().map(role -> Map.of("id",role.getId(),"name",role.getName())).collect(Collectors.toList()));
        body.put("tags", tags.stream().map(tag -> Map.of("id",tag.getId(),"name",tag.getName())).collect(Collectors.toList()));

        return  given().log().all()
                .body(body)
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
        body.put("roles", Arrays.asList(Map.of("id", role1.getId(), "name", role1.getName()),
                Map.of("id", role2.getId(), "name", role2.getName())));
        body.put("tags", Arrays.asList(Map.of("id", tag1.getId(), "name", tag1.getName()),
                Map.of("id", tag2.getId(), "name", tag2.getName())));


        given().body(body)
                .filter(RestAssuredRestDocumentationWrapper.document("create-team",
                        "팀 생성 API",
                        requestFields(fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("hostId").description("호스트 Id"),
                                fieldWithPath("tags").description("태그 리스트"),
                                fieldWithPath("roles").description("역할 리스트"),
                                fieldWithPath("roles[].id").description("역할 Id"),
                                fieldWithPath("roles[].name").description("역할 이름"),
                                fieldWithPath("tags[].id").description("태그 Id"),
                                fieldWithPath("tags[].name").description("태그 리스트")),
                        responseFields(fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("roleNames").description("팀 역할 구성"),
                                fieldWithPath("createdAt").description("팀 생성 시각"),
                                fieldWithPath("host.id").description("호스트 Id"),
                                fieldWithPath("host.nickName").description("호스트 닉네임"),
                                fieldWithPath("teamRoles").description("팀 역할 리스트"),
                                fieldWithPath("teamRoles[].id").description("팀 역할 Id"),
                                fieldWithPath("teamRoles[].teamId").ignored(),
                                fieldWithPath("teamRoles[].role").ignored(),
                                fieldWithPath("teamRoles[].role.id").ignored(),
                                fieldWithPath("teamRoles[].role.name").description("역할 이름"),
                                fieldWithPath("teamRoles[].applications").description("팀 역할에 지원한 지원서"),
                                fieldWithPath("tags").description("태그 리스트"),
                                fieldWithPath("recruiting").description("모집 여부"))))
                .when().post("/api/teams")
                .then().statusCode(201)
                .extract().body().as(TeamResponse.class);
    }




    
    @DisplayName("팀을 조회한다")
    @Test
    void readTeamTest() throws Exception{

        TeamResponse teamResponse =
                given()
                .filter(RestAssuredRestDocumentationWrapper.document("read-team",
                        "팀 조회 API",
                        responseFields(fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("roleNames").description("팀 역할 구성"),
                                fieldWithPath("createdAt").description("팀 생성 시각"),
                                fieldWithPath("host.id").description("호스트 Id"),
                                fieldWithPath("host.nickName").description("호스트 닉네임"),
                                fieldWithPath("tags").description("팀 태그"),
                                fieldWithPath("teamRoles").description("팀 역할"),
                                fieldWithPath("teamRoles[].id").description("팀 역할 Id"),
                                fieldWithPath("teamRoles[].teamId").ignored(),
                                fieldWithPath("teamRoles[].role").ignored(),
                                fieldWithPath("teamRoles[].role.id").ignored(),
                                fieldWithPath("teamRoles[].role.name").description("역할 이름"),
                                fieldWithPath("teamRoles[].applications").description("팀 역할에 지원한 지원서"),
                                fieldWithPath("recruiting").description("모집 여부"))))
                .when().get("/api/teams/{id}",teamResponse1.getTeamId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(TeamResponse.class);
    }

    @DisplayName("팀을 수정한다.")
    @Test
    void updateTeamTest() throws Exception{

        Role role3 = roleRepository.findById(3L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 1"));
        Role role4 = roleRepository.findById(4L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 2"));

        Tag tag3 = tagRepository.findById(3L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 3"));
        Tag tag4 = tagRepository.findById(4L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 4"));

        String updateTeamName = "KPOP";
        Map<String,Object> body = new HashMap<>();
        body.put("name",updateTeamName);
        body.put("roles", Arrays.asList(Map.of("id", role3.getId(), "name", role3.getName()),
                Map.of("id", role4.getId(), "name", role4.getName())));
        body.put("tags", Arrays.asList(Map.of("id", tag3.getId(), "name", tag3.getName()),
                Map.of("id", tag4.getId(), "name", tag4.getName())));
        body.put("isRecruiting",true);

        TeamResponse response = given().body(body)
                .filter(RestAssuredRestDocumentationWrapper.document("update-team",
                        "팀 업데이트 API",
                        requestFields(fieldWithPath("name").description("팀 이름"),
                                fieldWithPath("tags").description("팀 태그"),
                                fieldWithPath("roles").description("팀 역할"),
                                fieldWithPath("roles[].id").description("역할 Id"),
                                fieldWithPath("roles[].name").description("역할 이름"),
                                fieldWithPath("tags[].id").description("태그 Id"),
                                fieldWithPath("tags[].name").description("태그 리스트"),
                                fieldWithPath("isRecruiting").description("모집 여부")
                        ),
                        responseFields(fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("roleNames").description("팀 역할 구성"),
                                fieldWithPath("createdAt").description("팀 생성 시각"),
                                fieldWithPath("host.id").description("호스트 Id"),
                                fieldWithPath("host.nickName").description("호스트 닉네임"),
                                fieldWithPath("tags").description("팀 태그"),
                                fieldWithPath("teamRoles").description("팀 역할"),
                                fieldWithPath("teamRoles[].id").description("팀 역할 Id"),
                                fieldWithPath("teamRoles[].teamId").ignored(),
                                fieldWithPath("teamRoles[].role").ignored(),
                                fieldWithPath("teamRoles[].role.id").ignored(),
                                fieldWithPath("teamRoles[].role.name").description("역할 이름"),
                                fieldWithPath("teamRoles[].applications").description("팀 역할에 지원한 지원서"),
                                fieldWithPath("recruiting").description("모집 여부"))))
                .when().put("/api/teams/{id}", teamResponse1.getTeamId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(TeamResponse.class);

//
//        assertThat(response.getId()).isEqualTo(idToUpdate);
//        assertThat(response.getName()).isEqualTo(updateTagName);

    }

}
