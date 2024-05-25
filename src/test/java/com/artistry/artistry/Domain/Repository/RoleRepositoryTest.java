package com.artistry.artistry.Domain.Repository;

import com.artistry.artistry.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

//    @DisplayName("역할 더미 데이터를 확인한다.")
//    @ParameterizedTest
//    @CsvSource({"1,작곡가","2,일러스트레이터"})
//    void dummyRoleTest(Long id, String roleName){
//        Optional<Role> role = roleRepository.findById(id);
//        assertThat(role.isPresent()).isTrue();
//        assertThat(role.get().getRoleName()).isEqualTo(roleName);
//    }

}
