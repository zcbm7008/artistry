package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

//    @DisplayName("태그 더미 데이터를 확인한다.")
//    @ParameterizedTest
//    @CsvSource({"1,밴드","2,재즈"})
//    void dummyTagTest(Long id,String name){
//        Optional<TagResponse> tag = tagRepository.findById(id);
//        assertThat(tag.isPresent()).isTrue();
//        assertThat(tag.get().getName()).isEqualTo(name);
//    }
}
