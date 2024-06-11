package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.tag.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamUpdateRequest {

    @NonNull
    private String name;
    private List<TagRequest> tags;
    private List<RoleRequest> roles;
    private String teamStatus;

}
