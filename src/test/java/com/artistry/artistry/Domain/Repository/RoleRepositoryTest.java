package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @DisplayName("역할 더미 데이터를 확인한다.")
    @ParameterizedTest
    @CsvSource({"1,vocal","2,composer"})
    void dummyRoleTest(Long id, String roleName){
        Optional<Role> role = roleRepository.findById(id);
        assertThat(role.isPresent()).isTrue();
        assertThat(role.get().getRoleName()).isEqualTo(roleName);
    }

}
