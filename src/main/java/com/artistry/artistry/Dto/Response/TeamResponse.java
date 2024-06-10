package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.tag.Tag;
import com.artistry.artistry.Domain.team.Team;
import com.artistry.artistry.Domain.team.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {
    private Long teamId;
    private String teamName;
    private String createdAt;
    private HostResponse host;
    private List<String> tags;
    private List<TeamRoleResponse> teamRoles;
    private boolean recruiting;

    public static TeamResponse from(Team team){
        return TeamResponse.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .createdAt(team.getCreatedAt().toString())
                .host(HostResponse.from(team.getHost()))
                .teamRoles(team.getTeamRoles().stream().map(TeamRoleResponse::from).collect(Collectors.toList()))
                .tags(tagNames(team.getTags()))
                .recruiting(team.isRecruiting())
                .build();
    }

    public List<String> getRoleNames(){
        return teamRoles.stream().map(TeamRoleResponse::getRole)
                .map(RoleResponse::getName).collect(Collectors.toList());
    }

    private static <T> List<String> objectsToString(List<T> objects, Function<T,String> mapper){
        return objects.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    private static List<MemberResponse> memberNames(List<Member> applicants){
        if(applicants != null){
            return applicants.stream()
                    .map(MemberResponse::from)
                    .collect(Collectors.toList());
        }
        else{
            return null;
        }
    }

    private static List<String> tagNames(List<Tag> tags){
        if(tags == null){
            return Collections.singletonList(" ");
        }
        return objectsToString(tags,Tag::getName);
    }

    private static List<String> roleNames(List<TeamRole> roles){
        return objectsToString(roles, TeamRole::getRoleName);
    }

}
