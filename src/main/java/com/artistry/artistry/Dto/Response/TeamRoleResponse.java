package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.team.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRoleResponse {
    private Long id;
    private Long teamId;
    private RoleResponse role;
    private List<ApplicationResponse> applications;

    public static TeamRoleResponse from(TeamRole teamRole){
        return new TeamRoleResponse(
                teamRole.getId(),
                teamRole.getTeam().getId(),
                RoleResponse.from(teamRole.getRole()),
                teamRole.getApplications().stream()
                        .map(ApplicationResponse::from)
                        .collect(Collectors.toList()));
    }

}
