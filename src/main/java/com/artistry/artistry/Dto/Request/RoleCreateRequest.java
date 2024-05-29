package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.Role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateRequest {

    @NonNull
    private String name;

    public Role toEntity() {
        return new Role(name);
    }
}
