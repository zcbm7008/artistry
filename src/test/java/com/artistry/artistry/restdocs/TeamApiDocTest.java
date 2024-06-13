package com.artistry.artistry.restdocs;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamStatus;
import com.artistry.artistry.Dto.Request.ContentRequest;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Response.ApplicationResponse;
import com.artistry.artistry.Dto.Response.PortfolioResponse;
import com.artistry.artistry.Dto.Response.TeamResponse;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Service.ApplicationService;
import com.artistry.artistry.Service.TeamService;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class TeamApiDocTest extends ApiTest{
    @Autowired
    private TeamService teamService;
    @Autowired
    private ApplicationService applicationService;

    private Team dummyTeam;
    TeamResponse teamResponse1;
    TeamResponse teamResponse2;
    Role role1;
    @BeforeEach
    public void setUpData() {
        roleRepository.save(new Role("작곡가"));
        roleRepository.save(new Role("일러스트레이터"));
        roleRepository.save(new Role("작사가"));
        roleRepository.save(new Role("영상편집"));
        role1 = roleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 1"));
        Role role2 = roleRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 2"));

        tagRepository.save(new Tag("힙합"));
        tagRepository.save(new Tag("퓨처"));
        tagRepository.save(new Tag("하이퍼팝"));
        tagRepository.save(new Tag("디지코어"));
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
        teamResponse2 = create_team(dummyTeamName,member1.getId(),roles,tags);
    }

    public static TeamResponse create_team(String teamName,Long hostId, List<Role> roles,List<Tag> tags){
        Map<String, Object> body = new HashMap<>();
        body.put("name", teamName);
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

        role1 = roleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 1"));
        Role role2 = roleRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 2"));

        Tag tag1 = tagRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 1"));
        Tag tag2 = tagRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 2"));


        Map<String, Object> body = new HashMap<>();
        body.put("name", "팀1");
        body.put("hostId",1L);
        body.put("roles", Arrays.asList(Map.of("id", role1.getId(), "name", role1.getName()),
                Map.of("id", role2.getId(), "name", role2.getName())));
        body.put("tags", Arrays.asList(Map.of("id", tag1.getId(), "name", tag1.getName()),
                Map.of("id", tag2.getId(), "name", tag2.getName())));


        given().body(body)
                .filter(RestAssuredRestDocumentationWrapper.document("create-team",
                        "팀 생성 API",
                        requestFields(fieldWithPath("name").description("팀 이름"),
                                fieldWithPath("hostId").description("호스트 Id"),
                                fieldWithPath("tags").description("태그 리스트"),
                                fieldWithPath("roles").description("역할 리스트"),
                                fieldWithPath("roles[].id").description("역할 Id"),
                                fieldWithPath("roles[].name").description("역할 이름"),
                                fieldWithPath("tags[].id").description("태그 Id"),
                                fieldWithPath("tags[].name").description("태그 리스트")),
                        responseFields(fieldWithPath("id").description("팀 Id"),
                                fieldWithPath("name").description("팀 이름"),
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
                                fieldWithPath("teamStatus").description("팀 상태"))))
                .when().post("/api/teams")
                .then().statusCode(201)
                .extract().body().as(TeamResponse.class);
    }

    @DisplayName("팀을 조회한다")
    @Test
    void readTeamTest() throws Exception{

        TeamResponse response =
                given()
                .filter(RestAssuredRestDocumentationWrapper.document("read-team",
                        "팀 조회 API",
                        responseFields(fieldWithPath("id").description("팀 Id"),
                                fieldWithPath("name").description("팀 이름"),
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
                                fieldWithPath("teamStatus").description("팀 상태"))))
                .when().get("/api/teams/{id}",teamResponse1.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(TeamResponse.class);

        assertThat(response).usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(teamResponse1);

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
        body.put("teamStatus", "CANCELED");

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
                                fieldWithPath("teamStatus").description("팀 상태")
                        ),
                        responseFields(fieldWithPath("id").description("팀 Id"),
                                fieldWithPath("name").description("팀 이름"),
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
                                fieldWithPath("teamStatus").description("팀 상태"))))
                .when().put("/api/teams/{id}", teamResponse1.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(TeamResponse.class);

        System.out.println(new ObjectMapper().writeValueAsString(body));


        assertThat(response.getName()).isEqualTo(updateTeamName);
        assertThat(response.getRoleNames()).containsExactly(role3.getName(),role4.getName());
        assertThat(response.getTags()).containsExactly(tag3.getName(),tag4.getName());
        assertThat(response.getTeamStatus()).isEqualTo(String.valueOf(TeamStatus.CANCELED));

    }

    @DisplayName("팀의 상태를 cancel로 변경한다.")
    @Test
    void cancelTeam(){
        given().filter(RestAssuredRestDocumentationWrapper.document("delete-team",
                        "팀 모집 취소 API"))
                .when().delete("/api/teams/{id}/cancel",teamResponse1.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());

        int statusCode =  given()
                .when().get("/api/teams/{id}", teamResponse1.getId())
                .then().extract().statusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.NOT_FOUND.value());

        assertThatThrownBy(() -> teamService.findEntityById(teamResponse1.getId())).isInstanceOf(TeamNotFoundException.class);

    }

    @DisplayName("팀의 상태를 finish로 변경한다.")
    @Test
    void finishTeam(){
        String title = "작곡가 포트폴리오1";
        Member member1 = memberRepository.save(new Member("member1","a@a.com","a.url"));
        Map<String, Object> body2 = getStringObjectMap(role1, title,"PUBLIC");
        body2.put("memberId",member1.getId());

        PortfolioResponse portfolioResponse =
                given().body(body2)
                        .when().post("/api/portfolios")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(PortfolioResponse.class);

        Map<String,Object> body = new HashMap<>();
        body.put("id",portfolioResponse.getId());

        ApplicationResponse appResponse = given().body(body)
                .when().put("/api/teams/{id}/applications", teamResponse1.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(ApplicationResponse.class);

        applicationService.changedApplicationStatus(appResponse.getId(), ApplicationStatus.APPROVED);

        TeamResponse response = given().filter(RestAssuredRestDocumentationWrapper.document("finish-team",
                        "팀 모집 완료 API"))
                .when().put("/api/teams/{id}/finish",teamResponse1.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(TeamResponse.class);

        List<String> statuses = response.getTeamRoles().stream()
                .flatMap(teamRole -> teamRole.getApplications().stream())
                .map(ApplicationResponse::getStatus)
                .collect(Collectors.toList());

        assertThat(statuses).containsOnly("APPROVED");
        assertThat(response.getTeamStatus()).isEqualTo(TeamStatus.FINISHED.toString());
    }

    @DisplayName("팀에 포트폴리오를 지원한다.")
    @Test
    void applyTeam() throws Exception{
        String title = "작곡가 포트폴리오1";
        Member member1 = memberRepository.save(new Member("member1","a@a.com","a.url"));
        Map<String, Object> body2 = getStringObjectMap(role1, title,"PUBLIC");
        body2.put("memberId",member1.getId());

        PortfolioResponse portfolioResponse =
                given().body(body2)
                        .when().post("/api/portfolios")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(PortfolioResponse.class);

        Map<String,Object> body = new HashMap<>();
        body.put("id",portfolioResponse.getId());

        ApplicationResponse response = given().body(body)
                .filter(RestAssuredRestDocumentationWrapper.document("apply-team",
                        "팀 지원 API",
                        requestFields(fieldWithPath("id").description("포트폴리오 id")
                        ),
                        responseFields(fieldWithPath("id").description("지원서 id"),
                                fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("role").description("지원한 역할"),
                                fieldWithPath("member.id").description("지원한 멤버 id"),
                                fieldWithPath("member.nickName").description("지원한 멤버 닉네임"),
                                fieldWithPath("member.email").description("지원한 멤버 이메일"),
                                fieldWithPath("member.iconUrl").description("지원한 멤버 아이콘 url"),
                                fieldWithPath("portfolio.id").description("포트폴리오 id"),
                                fieldWithPath("portfolio.title").description("포트폴리오 타이틀"),
                                fieldWithPath("portfolio.roleName").ignored(),
                                fieldWithPath("portfolio.member").description("포트폴리오 소유 멤버"),
                                fieldWithPath("portfolio.member.id").description("멤버 id"),
                                fieldWithPath("portfolio.member.nickName").description("멤버 닉네임"),
                                fieldWithPath("portfolio.member.email").description("멤버 이메일"),
                                fieldWithPath("portfolio.member.iconUrl").description("멤버 아이콘 url"),
                                fieldWithPath("portfolio.contents").description("포트폴리오 컨텐츠"),
                                fieldWithPath("portfolio.contents[].url").description("포트폴리오 컨텐츠 url"),
                                fieldWithPath("portfolio.contents[].comment").description("포트폴리오 컨텐츠 코멘트"),
                                fieldWithPath("portfolio.access").description("포트폴리오 공개 여부"),
                                fieldWithPath("status").description("포트폴리오 지원 상태"))))
                .when().put("/api/teams/{id}/applications", teamResponse1.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(ApplicationResponse.class);

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
