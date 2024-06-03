package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.team.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberTeamsResponse {
    List<TeamResponse> teamResponses;

    public static MemberTeamsResponse from(List<Team> teams){
        return new MemberTeamsResponse(teams.stream().map(TeamResponse::from).toList());
    }
}
