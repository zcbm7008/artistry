package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Dto.Request.TagCreateRequest;
import com.artistry.artistry.Dto.Request.TagUpdateRequest;
import com.artistry.artistry.Dto.Response.TagNameResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Exceptions.TagNotFoundException;
import com.artistry.artistry.Repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class TagServiceTest {
    @Autowired
    private TagService tagService;
    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll(); // 기존 데이터 삭제
    }


    @DisplayName("Tag Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> tagService.findById(Long.MAX_VALUE))
                .isInstanceOf(TagNotFoundException.class);
    }

    @DisplayName("태그 전체를 불러온다.")
    @Test
    void getAllTags(){
        prepareDummyTags();

        List<String> expectedTags = Arrays.asList("트랩","힙합","밴드");

        List<String> allTags = tagService.findAll()
                .stream()
                .map(TagResponse::getName)
                .collect(Collectors.toList());

        assertThat(expectedTags).usingRecursiveComparison().isEqualTo(allTags);
    }

    private void prepareDummyTags() {
        tagRepository.save(new Tag("트랩"));
        tagRepository.save(new Tag("힙합"));
        tagRepository.save(new Tag("밴드"));
        tagRepository.save(new Tag("재즈밴드"));
    }
    
    @DisplayName("태그 이름으로 검색한다.")
    @Test
    void findTagNameByName(){
        prepareDummyTags();
        String findName = "밴드";
        List <String> foundTagNames = tagService.findTagNames(findName)
                .stream()
                .map(TagNameResponse::getName)
                .collect(Collectors.toList());

        assertThat(foundTagNames).hasSize(2)
                .contains("밴드","재즈밴드");
    }

    @DisplayName("태그를 생성한다.")
    @Test
    void createTag(){
        String tagName = "퓨처리딤";
        TagCreateRequest request = new TagCreateRequest(tagName);

        TagResponse response = tagService.createTag(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(tagName);
    }

    @DisplayName("태그 정보를 수정할 때")
    @Nested
    class UpdateTag{

        @DisplayName("id에 해당하는 태그가 존재하면 해당 태그 정보를 수정한다.")
        @Test
        void updateTag(){

            // given
            Tag tag = tagRepository.save(new Tag("태그1"));
            String updatedTagName = "태그1업데이트";

            //when
            TagUpdateRequest request = new TagUpdateRequest(updatedTagName);
            TagResponse response = tagService.updateTag(tag.getId(),request);

            // then
            assertThat(response.getId()).isEqualTo(tag.getId());
            assertThat(response.getName()).isEqualTo(updatedTagName);
        }
        
        @DisplayName("id에 해당하는 태그가 없으면 예외가 발생한다.")
        @Test
        void updateTagException() {
            String updateTagName = "태그1업데이트";

            TagUpdateRequest request = new TagUpdateRequest(updateTagName);
            assertThatThrownBy(()->tagService.updateTag(Long.MAX_VALUE,request))
                    .isExactlyInstanceOf(TagNotFoundException.class);
        }
    }


}
