package com.artistry.artistry.Service;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Exceptions.RoleNotFoundException;
import com.artistry.artistry.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role findById(Long id){
        return roleRepository.findById(id)
                .orElseThrow(RoleNotFoundException::new);
    }

}
