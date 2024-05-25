package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Dto.Request.RoleCreateRequest;
import com.artistry.artistry.Dto.Request.RoleUpdateRequest;
import com.artistry.artistry.Dto.Request.TagCreateRequest;
import com.artistry.artistry.Dto.Request.TagUpdateRequest;
import com.artistry.artistry.Dto.Response.RoleResponse;
import com.artistry.artistry.Dto.Response.TagNameResponse;
import com.artistry.artistry.Dto.Response.TagResponse;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Exceptions.TagNotFoundException;
import com.artistry.artistry.Repository.RoleRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@Transactional
@SpringBootTest
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;


    @BeforeEach
    void setUp() {
        roleRepository.deleteAll(); // 기존 데이터 삭제
    }


    @DisplayName("Role Id가 없을경우 예외를 던짐.")
    @Test
    void roleNotFound(){
        assertThatThrownBy(() -> roleService.findById(Long.MAX_VALUE))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @DisplayName("역할 전체를 불러온다.")
    @Test
    void getAllRoles(){
        prepareDummyRoles();

        List<String> expectedTags = Arrays.asList("작곡가","보컬","영상편집자");

        List<String> allRoles = roleService.findAll()
                .stream()
                .map(RoleResponse::getName)
                .toList();

        assertThat(expectedTags).usingRecursiveComparison().isEqualTo(allRoles);
    }

    private void prepareDummyRoles() {
        roleRepository.save(new Role("작곡가"));
        roleRepository.save(new Role("보컬"));
        roleRepository.save(new Role("영상편집자"));

    }

    @DisplayName("역할을 생성한다.")
    @Test
    void createRole(){
        String roleName = "일러스트레이터";
        RoleCreateRequest request = new RoleCreateRequest(roleName);

        RoleResponse response = roleService.createRole(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo(roleName);
    }

    @DisplayName("역할 정보를 수정할 때")
    @Nested
    class UpdateRole{

        @DisplayName("id에 해당하는 태그가 존재하면 해당 역할 정보를 수정한다.")
        @Test
        void updateRole(){

            // given
            Role role = roleRepository.save(new Role("역할1"));
            String updatedRoleName = "업데이트 역할1";

            //when
            RoleUpdateRequest request = new RoleUpdateRequest(updatedRoleName);
            RoleResponse response = roleService.updateRole(role.getId(),request);

            // then
            assertThat(response.getId()).isEqualTo(role.getId());
            assertThat(response.getName()).isEqualTo(updatedRoleName);
        }

        @DisplayName("id에 해당하는 역할이 없으면 예외가 발생한다.")
        @Test
        void updateRoleException() {
            String updateTagName = "역할1업데이트";

            RoleUpdateRequest request = new RoleUpdateRequest(updateTagName);
            assertThatThrownBy(()->roleService.updateRole(Long.MAX_VALUE,request))
                    .isExactlyInstanceOf(RoleNotFoundException.class);
        }
    }

    @DisplayName("역할을 삭제할 때")
    @Nested
    class DeleteRole{
        @DisplayName("id에 해당하는 역할이 존재하면 해당 역할을 삭제한다.")
        @Test
        void deleteRole() {
            //given
            Role role = roleRepository.save(new Role("잘못만든역할"));

            //when
            assertThat(roleRepository.findById(role.getId())).isPresent();
            roleService.deleteRole(role.getId());

            //Then
            assertThat(roleRepository.findById(role.getId())).isNotPresent();

        }

        @DisplayName("id에 해당하는 역할이 없으면 예외가 발생한다.")
        @Test
        void deleteTagException() {
            assertThatThrownBy(() -> roleService.deleteRole(Long.MAX_VALUE))
                    .isExactlyInstanceOf(RoleNotFoundException.class);
        }
    }
}
