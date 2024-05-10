package com.artistry.artistry.service;

import com.artistry.artistry.Exceptions.MemberNotFoundException;
import com.artistry.artistry.Service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("Member Id가 없을경우 예외를 던짐.")
    @Test
    void memberNotFound(){
        assertThatThrownBy(() -> memberService.findById(Long.MAX_VALUE))
                .isInstanceOf(MemberNotFoundException.class);
    }

}