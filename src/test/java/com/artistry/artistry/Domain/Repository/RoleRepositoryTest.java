package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @DisplayName("역할을 생성한다.")
    @Test
    void saveRole() {
        Role role = new Role("새 태그1");
        Role savedRole = roleRepository.save(role);

        assertThat(savedRole.getId()).isNotNull();
        assertThat(savedRole.getName()).isEqualTo(role.getName());
    }

}
