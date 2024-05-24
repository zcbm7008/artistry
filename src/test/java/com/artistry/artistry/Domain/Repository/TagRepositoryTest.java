package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("태그를 생성한다.")
    @Test
    void saveTag() {
        Tag tag = new Tag("새 태그1");
        Tag savedTag = tagRepository.save(tag);

        assertThat(savedTag.getId()).isNotNull();
        assertThat(savedTag.getName()).isEqualTo(tag.getName());
    }

    @DisplayName("태그 이름을 검색한다.")
    @Test
    void findByName(){
        Tag tag1 = tagRepository.save(new Tag("힙합"));
        Tag tag2 = tagRepository.save(new Tag("재즈힙합"));

        List <Tag> foundTags = tagRepository.findAllByName("힙합");

        assertThat(foundTags).hasSize(2)
                .contains(tag1,tag2);
    }
}
