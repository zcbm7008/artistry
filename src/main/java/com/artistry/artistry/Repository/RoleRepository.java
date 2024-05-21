package com.artistry.artistry.Repository;

import com.artistry.artistry.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role,Long> {
}
