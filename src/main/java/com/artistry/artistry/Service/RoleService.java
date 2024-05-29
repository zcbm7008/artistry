package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Dto.Request.RoleCreateRequest;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Dto.Request.RoleUpdateRequest;
import com.artistry.artistry.Dto.Response.RoleResponse;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {


    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public RoleResponse findById(Long id){
        return RoleResponse.from(findRoleById(id));
    }

    public Role findRoleById(Long id){
        return roleRepository.findById(id)
                .orElseThrow(RoleNotFoundException::new);
    }

    public List<Role> findAllById(final List<RoleRequest> roleRequests){
        return roleRequests.stream()
                .map(roleRequest -> findRoleById(roleRequest.getId()))
                .collect(Collectors.toList());
    }
    public List<RoleResponse> findAll() {
        List <Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(RoleResponse::from)
                .collect(Collectors.toList());
    }

    public RoleResponse createRole(final RoleCreateRequest request){
        Role role = roleRepository.save(request.toEntity());

        return RoleResponse.from(role);
    }

    @Transactional
    public RoleResponse updateRole(final Long roleId, final RoleUpdateRequest request){
        Role role = findRoleById(roleId);
        role.update(request.getName());

        return RoleResponse.from(findRoleById(roleId));
    }

    @Transactional
    public void deleteRole(final Long roleId){
        Role role = findRoleById(roleId);
        roleRepository.delete(role);
    }

}
