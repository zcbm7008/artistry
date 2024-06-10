package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.Role.Role;
import com.artistry.artistry.Domain.tag.Tag;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamUpdateRequest {

    @NonNull
    String name;

    List<TagRequest> tags;

    List<RoleRequest> roles;

    boolean isRecruiting;
}
