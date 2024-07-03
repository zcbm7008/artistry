package com.artistry.artistry.restdocs;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.application.ApplicationStatus;
import com.artistry.artistry.Domain.application.ApplicationType;
import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.member.MemberLink;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Dto.Request.*;
import com.artistry.artistry.Dto.Response.*;
import com.artistry.artistry.Service.ApplicationService;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class ApplicationApiDocTest extends ApiTest{
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    ApplicationService applicationService;

    Role role1;
    Member host;
    Member applicant;
    Team dummyTeam;
    TeamResponse dummyTeamResponse;

    PortfolioResponse portfolioResponse;
    AccessTokenResponse hostTokenResponse;
    AccessTokenResponse applicantTokenResponse;
    ApplicationResponse dummyAppResponse;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation){
        super.setUp(restDocumentation);

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

        Member member = new Member("member1","a@a.com");
        member.getMemberLinks().add(new MemberLink("a.url","soundcloud"));
        host = memberRepository.save(member);
        applicant = memberRepository.save(new Member("applicant","app@app.com","a.url"));

        Map<String, Object> body = getStringObjectMap(role1, "포폴1","PUBLIC");
        body.put("memberId",applicant.getId());

        portfolioResponse = getPortfolioResponse(given().body(body)
                .when().post("/api/portfolios"));

        // 더미 팀 생성
        String dummyTeamName = "더미 팀";
        List<Role> roles = Arrays.asList(role1, role2);
        List<Tag> tags = Arrays.asList(tag1, tag2);

        dummyTeamResponse = create_team(dummyTeamName, host.getId(),roles,tags);
        System.out.println(dummyTeamResponse.getId());

        ApplicationCreateRequest request =
                new ApplicationCreateRequest(new TeamInfoRequest(dummyTeamResponse.getId()),
                        new RoleRequest(role2.getId()),
                        new PortfolioRequest(portfolioResponse.getId()),
                        ApplicationStatus.PENDING.toString(),
                        ApplicationType.APPLICATION.toString());

       dummyAppResponse = applicationService.createApplication(applicant.getId(),request);
    }

    @DisplayName("지원서를 생성한다.")
    @Test
    void createApplication(){
        ApplicationCreateRequest request =
                new ApplicationCreateRequest(
                        new TeamInfoRequest(dummyTeamResponse.getId()),
                        new RoleRequest(role1.getId()),
                        new PortfolioRequest(portfolioResponse.getId()),
                        ApplicationStatus.PENDING.toString(),
                        ApplicationType.APPLICATION.toString());

        Map<String, Object> body = new HashMap<>();
        body.put("team", request.getTeam());
        body.put("role", request.getRole());
        body.put("portfolio", request.getPortfolio());
        body.put("status", request.getStatus());
        body.put("type", request.getType());

        String accessToken = getAccessToken(applicant.getEmail());

        ApplicationResponse response =
                given().body(body)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .filter(RestAssuredRestDocumentationWrapper.document("create-application",
                                "지원서 생성 API",
                                requestFields(
                                        fieldWithPath("team.id").description("팀 id"),
                                        fieldWithPath("role.id").description("역할 id"),
                                        fieldWithPath("portfolio.id").description("포트폴리오 id"),
                                        fieldWithPath("status").description("지원서 상태"),
                                        fieldWithPath("type").description("지원서 타입")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("지원서 id"),
                                        fieldWithPath("teamId").description("지원서 팀 id"),
                                        fieldWithPath("role").description("지원 역할"),
                                        fieldWithPath("portfolio").description("포트폴리오"),
                                        fieldWithPath("portfolio.id").description("포트폴리오 id"),
                                        fieldWithPath("portfolio.title").description("포트폴리오 제목"),
                                        fieldWithPath("portfolio.member").description("포트폴리오 멤버"),
                                        fieldWithPath("portfolio.member.id").description("포트폴리오 멤버 id"),
                                        fieldWithPath("portfolio.member.nickName").description("포트폴리오 멤버 닉네임"),
                                        fieldWithPath("portfolio.member.email").description("포트폴리오 멤버 이메일"),
                                        fieldWithPath("portfolio.member.iconUrl").description("포트폴리오 멤버 아이콘 url"),
                                        fieldWithPath("portfolio.member.bio").description("포트폴리오 멤버 자기소개"),
                                        fieldWithPath("portfolio.member.links").description("포트폴리오 멤버 sns 링크"),
                                        fieldWithPath("portfolio.roleName").description("포트폴리오 지원 역할"),
                                        fieldWithPath("portfolio.contents").description("포트폴리오 컨텐츠"),
                                        fieldWithPath("portfolio.contents[].url").description("포트폴리오 컨텐츠 url"),
                                        fieldWithPath("portfolio.contents[].comment").description("포트폴리오 컨텐츠 코멘트"),
                                        fieldWithPath("portfolio.access").description("포트폴리오 공개 여부"),
                                        fieldWithPath("portfolio.view").description("포트폴리오 조회수"),
                                        fieldWithPath("portfolio.like").description("포트폴리오 좋아요"),
                                        fieldWithPath("status").description("지원서 상태"),
                                        fieldWithPath("type").description("지원서 타입"))))
                        .when().post("/api/applications")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(ApplicationResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.PENDING.toString());
        assertThat(response.getType()).isEqualTo(ApplicationType.APPLICATION.toString());
    }

    @DisplayName("제안서를 생성한다.")
    @Test
    void createInvitation(){
        ApplicationCreateRequest request =
                new ApplicationCreateRequest(
                        new TeamInfoRequest(dummyTeamResponse.getId()),
                        new RoleRequest(role1.getId()),
                        new PortfolioRequest(portfolioResponse.getId()),
                        ApplicationStatus.PENDING.toString(),
                        ApplicationType.INVITATION.toString());

        Map<String, Object> body = new HashMap<>();
        body.put("team", request.getTeam());
        body.put("role", request.getRole());
        body.put("portfolio", request.getPortfolio());
        body.put("status", request.getStatus());
        body.put("type", request.getType());

        String accessToken = getAccessToken(host.getEmail());

        ApplicationResponse response =
                given().body(body)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .filter(RestAssuredRestDocumentationWrapper.document("create-application",
                                "제안서 생성 API",
                                requestFields(
                                        fieldWithPath("team.id").description("팀 id"),
                                        fieldWithPath("role.id").description("역할 id"),
                                        fieldWithPath("portfolio.id").description("포트폴리오 id"),
                                        fieldWithPath("status").description("지원서 상태"),
                                        fieldWithPath("type").description("지원서 타입")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("지원서 id"),
                                        fieldWithPath("teamId").description("지원서 팀 id"),
                                        fieldWithPath("role").description("지원 역할"),
                                        fieldWithPath("portfolio").description("포트폴리오"),
                                        fieldWithPath("portfolio.id").description("포트폴리오 id"),
                                        fieldWithPath("portfolio.title").description("포트폴리오 제목"),
                                        fieldWithPath("portfolio.member").description("포트폴리오 멤버"),
                                        fieldWithPath("portfolio.member.id").description("포트폴리오 멤버 id"),
                                        fieldWithPath("portfolio.member.nickName").description("포트폴리오 멤버 닉네임"),
                                        fieldWithPath("portfolio.member.email").description("포트폴리오 멤버 이메일"),
                                        fieldWithPath("portfolio.member.iconUrl").description("포트폴리오 멤버 아이콘 url"),
                                        fieldWithPath("portfolio.member.bio").description("포트폴리오 멤버 자기소개"),
                                        fieldWithPath("portfolio.member.links").description("포트폴리오 멤버 sns 링크"),
                                        fieldWithPath("portfolio.roleName").description("포트폴리오 지원 역할"),
                                        fieldWithPath("portfolio.contents").description("포트폴리오 컨텐츠"),
                                        fieldWithPath("portfolio.contents[].url").description("포트폴리오 컨텐츠 url"),
                                        fieldWithPath("portfolio.contents[].comment").description("포트폴리오 컨텐츠 코멘트"),
                                        fieldWithPath("portfolio.access").description("포트폴리오 공개 여부"),
                                        fieldWithPath("portfolio.view").description("포트폴리오 조회수"),
                                        fieldWithPath("portfolio.like").description("포트폴리오 좋아요"),
                                        fieldWithPath("status").description("지원서 상태"),
                                        fieldWithPath("type").description("지원서 타입"))))
                        .when().post("/api/applications")
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(ApplicationResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.PENDING.toString());
        assertThat(response.getType()).isEqualTo(ApplicationType.INVITATION.toString());
    }

    @DisplayName("지원서의 상태를 바꾼다.")
    @Test
    void changeApplicationStatus() {

        Map<String, Object> body = new HashMap<>();
        body.put("status", ApplicationStatus.APPROVED.toString());

        String accessToken = getAccessToken(host.getEmail());

        ApplicationResponse response =
                given().body(body)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .filter(RestAssuredRestDocumentationWrapper.document("update-application",
                                "지원서 상태 변경 API",
                                requestFields(
                                        fieldWithPath("status").description("지원서 상태")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("지원서 id"),
                                        fieldWithPath("teamId").description("지원서 팀 id"),
                                        fieldWithPath("role").description("지원 역할"),
                                        fieldWithPath("portfolio").description("포트폴리오"),
                                        fieldWithPath("portfolio.id").description("포트폴리오 id"),
                                        fieldWithPath("portfolio.title").description("포트폴리오 제목"),
                                        fieldWithPath("portfolio.member").description("포트폴리오 멤버"),
                                        fieldWithPath("portfolio.member.id").description("포트폴리오 멤버 id"),
                                        fieldWithPath("portfolio.member.nickName").description("포트폴리오 멤버 닉네임"),
                                        fieldWithPath("portfolio.member.email").description("포트폴리오 멤버 이메일"),
                                        fieldWithPath("portfolio.member.iconUrl").description("포트폴리오 멤버 아이콘 url"),
                                        fieldWithPath("portfolio.member.bio").description("포트폴리오 멤버 자기소개"),
                                        fieldWithPath("portfolio.member.links").description("포트폴리오 멤버 sns 링크"),
                                        fieldWithPath("portfolio.roleName").description("포트폴리오 지원 역할"),
                                        fieldWithPath("portfolio.contents").description("포트폴리오 컨텐츠"),
                                        fieldWithPath("portfolio.contents[].url").description("포트폴리오 컨텐츠 url"),
                                        fieldWithPath("portfolio.contents[].comment").description("포트폴리오 컨텐츠 코멘트"),
                                        fieldWithPath("portfolio.access").description("포트폴리오 공개 여부"),
                                        fieldWithPath("portfolio.view").description("포트폴리오 조회수"),
                                        fieldWithPath("portfolio.like").description("포트폴리오 좋아요"),
                                        fieldWithPath("status").description("지원서 상태"),
                                        fieldWithPath("type").description("지원서 타입"))))
                        .when().patch("/api/applications/" + dummyAppResponse.getId())
                        .then().statusCode(HttpStatus.OK.value())
                        .extract().body().as(ApplicationResponse.class);

        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.APPROVED.toString());

    }

    private static String getAccessToken(String email) {
        AccessTokenResponse accessTokenResponse = given().when().get("/api/fake/auth/tokens?email=" + email)
                .then().log().all()
                .extract()
                .as(AccessTokenResponse.class);

        return accessTokenResponse.getAccessToken();
    }

    public static TeamResponse create_team(String teamName, Long hostId, List<Role> roles, List<Tag> tags){
        Map<String, Object> body = new HashMap<>();
        body.put("name", teamName);
        body.put("hostId",hostId);
        body.put("roles", roles.stream().map(role -> Map.of("id",role.getId(),"name",role.getName())).collect(Collectors.toList()));
        body.put("tags", tags.stream().map(tag -> Map.of("id",tag.getId(),"name",tag.getName())).collect(Collectors.toList()));

        return given().log().all()
                .body(body)
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/api/teams")
                .then().statusCode(201)
                .extract().body().as(TeamResponse.class);
    }

    private static PortfolioResponse getPortfolioResponse(Response body2) {
        return body2
                .then().statusCode(HttpStatus.OK.value())
                .extract().body().as(PortfolioResponse.class);
    }

    private static Map<String, Object> getStringObjectMap(Role role1, String title,String access) {
        RoleRequest roleRequest = new RoleRequest(role1.getId()); // Create and populate RoleRequest object as needed
        List<LinkRequest> contents =
                List.of(new LinkRequest("https://www.youtube.com/watch?v=N9_hsXleJgs","fantasize"),
                        new LinkRequest("https://www.youtube.com/watch?v=RyZz1JX8xC8","victim")); // Create and populate ContentRequest list as needed

        Map<String,Object> body =new HashMap<>();
        body.put("title", title);
        body.put("role",roleRequest);
        body.put("contents",contents);
        body.put("access", access);
        return body;
    }
}
