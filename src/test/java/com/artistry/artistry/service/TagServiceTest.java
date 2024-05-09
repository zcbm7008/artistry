package com.artistry.artistry.service;

import com.artistry.artistry.Exceptions.TagNotFoundException;
import com.artistry.artistry.Exceptions.TeamNotFoundException;
import com.artistry.artistry.Service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TagServiceTest {
    @Autowired
    private TagService tagService;

    @DisplayName("Tag Id가 없을경우 예외를 던짐.")
    @Test
    void teamNotFound(){
        assertThatThrownBy(() -> tagService.findById(Long.MAX_VALUE))
                .isInstanceOf(TagNotFoundException.class);
    }


}
