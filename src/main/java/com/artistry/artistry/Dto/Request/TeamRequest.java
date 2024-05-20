package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {
    @NonNull
    private String teamName;
    private List<Role> roles;
    @NonNull
    private Long hostId;
    private List<Tag> tags;
}
