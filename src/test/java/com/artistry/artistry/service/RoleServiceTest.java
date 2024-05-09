package com.artistry.artistry.service;

import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Exceptions.TagNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @DisplayName("Role Id가 없을경우 예외를 던짐.")
    @Test
    void roleNotFound(){
        assertThatThrownBy(() -> roleService.findById(Long.MAX_VALUE)
                .isInstanceOf(RoleNotFoundException.class));
    }
}
