package com.artistry.artistry.Service;

import com.artistry.artistry.Exceptions.RoleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
@Transactional
@SpringBootTest
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @DisplayName("Role Id가 없을경우 예외를 던짐.")
    @Test
    void roleNotFound(){
        assertThatThrownBy(() -> roleService.findById(Long.MAX_VALUE))
                .isInstanceOf(RoleNotFoundException.class);
    }
}
