package com.artistry.artistry.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {
    @NonNull
    private String teamName;
    @NonNull
    private Long hostId;
    private List<TagRequest> tags;
    private List<RoleRequest> roles;

}
