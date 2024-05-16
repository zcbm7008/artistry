package com.artistry.artistry.restdocs;

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
public class UserApiDocTest{

    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @Autowired
    private TagRepository tagRepository;

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


        Map<String, Object> body = new HashMap<>();
        body.put("teamId", 1L);
        body.put("teamName", "팀1");
        body.put("hostId",1L);
        body.put("tags", Arrays.asList(tagRepository.findById(1L), tagRepository.findById(2L)));

        mockMvc.perform(post("/api/teams")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamId").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.host.id").value(1))
                .andExpect(jsonPath("$.host.name").exists())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags",hasSize(2)))
                .andExpect(jsonPath("$.tags",hasItem("band")))
                .andExpect(jsonPath("$.tags",hasItem("edm")))
                .andDo(document("create-team",
                        requestFields(fieldWithPath("teamName").description("팀 이름"),
                                fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("hostId").description("호스트 Id"),
                                fieldWithPath("tags").description("태그 리스트"),
                                fieldWithPath("tags[].id").description("태그 Id"),
                                fieldWithPath("tags[].name").description("태그 리스트")),
                        responseFields(fieldWithPath("teamId").description("팀 Id"),
                                fieldWithPath("createdAt").description("팀 생성 시각"),
                                fieldWithPath("host.id").description("호스트 Id"),
                                fieldWithPath("host.name").description("호스트 닉네임"),
                                fieldWithPath("tags").description("태그 리스트"),
                                fieldWithPath("members").description("멤버 리스트"),
                                fieldWithPath("roles").description("역할 리스트"))));

    }
}
