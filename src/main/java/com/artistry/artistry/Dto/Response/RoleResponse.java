package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private Long id;
    private String roleName;

    public static RoleResponse from(Role role){
        return new RoleResponse(role.getId(), role.getRoleName());
    }
}
