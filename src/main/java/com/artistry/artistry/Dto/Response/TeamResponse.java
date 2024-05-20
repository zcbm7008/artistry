package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {
    private Long teamId;
    private String createdAt;

    private HostResponse host;
    private List<MemberResponse> members;
    private List<String> tags;
    private List<String> roles;

    public static TeamResponse from(Team team){
        return TeamResponse.builder()
                .teamId(team.getId())
                .createdAt(team.getCreatedAt().toString())
                .host(HostResponse.from(team.getHost()))
                .roles(roleNames(team.getTeamRoles()))
                .tags(tagNames(team.getTags()))
                .build();
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
        return objectsToString(tags,Tag::getName);
    }

    private static List<String> roleNames(List<TeamRole> roles){
        return objectsToString(roles, TeamRole::getRoleName);
    }

}
