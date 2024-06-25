package com.artistry.artistry.Dto.Request;

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
