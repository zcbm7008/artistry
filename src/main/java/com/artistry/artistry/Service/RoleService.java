package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Dto.Request.RoleRequest;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {


    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role findById(Long id){
        return roleRepository.findById(id)
                .orElseThrow(RoleNotFoundException::new);
    }

    public List<Role> findAllById(final List<RoleRequest> roleRequests){
        return roleRequests.stream()
                .map(roleRequest -> findById(roleRequest.getId()))
                .collect(Collectors.toList());
    }

}
