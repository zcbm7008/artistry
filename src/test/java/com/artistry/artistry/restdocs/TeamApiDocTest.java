package com.artistry.artistry.restdocs;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Repository.RoleRepository;
import com.artistry.artistry.Repository.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
public class TeamApiDocTest {

    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .build();


    }

    @DisplayName("팀을 생성한다")
    @Test
    void createTeam() throws Exception{
        Role role1 = roleRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 1"));
        Role role2 = roleRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid role ID 2"));
        Tag tag1 = tagRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 1"));
        Tag tag2 = tagRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Invalid tag ID 2"));


        Map<String, Object> body = new HashMap<>();
        body.put("teamId", 1L);
        body.put("teamName", "팀1");
        body.put("hostId",1L);
        body.put("roles",Arrays.asList(role1,role2));
        body.put("tags", Arrays.asList(tag1,tag2));

        mockMvc.perform(post("/api/teams")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamId").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.host.id").value(1))
                .andExpect(jsonPath("$.host.nickName").exists())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags",hasSize(2)))
                .andExpect(jsonPath("$.tags",hasItem("band")))
                .andExpect(jsonPath("$.tags",hasItem("edm")))
                .andExpect(jsonPath("$.teamRoles").isArray())
                .andExpect(jsonPath("$.teamRoles",hasSize(2)))
                .andDo(document("create-team",
                        requestFields(fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("hostId").description("호스트 Id"),
                                fieldWithPath("tags").description("태그 리스트"),
                                fieldWithPath("roles").description("역할 리스트"),
                                fieldWithPath("roles[].id").description("역할 Id"),
                                fieldWithPath("roles[].roleName").description("역할 이름"),
                                fieldWithPath("roles[].teamRoles").ignored(),
                                fieldWithPath("tags[].id").description("태그 Id"),
                                fieldWithPath("tags[].name").description("태그 리스트")),
                        responseFields(fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("createdAt").description("팀 생성 시각"),
                                fieldWithPath("host.id").description("호스트 Id"),
                                fieldWithPath("host.nickName").description("호스트 닉네임"),
                                fieldWithPath("teamRoles").description("팀 역할 리스트"),
                                fieldWithPath("teamRoles[].id").description("팀 역할 Id"),
                                fieldWithPath("teamRoles[].teamId").description("팀 Id"),
                                fieldWithPath("teamRoles[].role").description("역할"),
                                fieldWithPath("teamRoles[].role.id").description("역할 Id"),
                                fieldWithPath("teamRoles[].role.roleName").description("역할 이름"),
                                fieldWithPath("teamRoles[].applications").description("팀 역할에 지원한 지원서"),
                                fieldWithPath("tags").description("태그 리스트"))));

    }
}