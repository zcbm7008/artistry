package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.Member;
import com.artistry.artistry.Domain.Role;
import com.artistry.artistry.Domain.Tag;
import com.artistry.artistry.Domain.Team;
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
public class TeamResponseDto {
    private Long teamId;
    private String createdAt;

    private HostResponseDto host;
    private List<MemberResponseDto> members;
    private List<String> tags;
    private List<String> roles;

    public static TeamResponseDto from(Team team){
        return TeamResponseDto.builder()
                .teamId(team.getId())
                .createdAt(team.getCreatedAt().toString())
                .members(memberNames(team.getApplicants()))
                .host(HostResponseDto.from(team.getHost()))
                .tags(tagNames(team.getTags()))
                .roles(roleNames(team.getRoles()))
                .build();
    }

    private static <T> List<String> objectsToString(List<T> objects, Function<T,String> mapper){
        return objects.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    private static List<MemberResponseDto> memberNames(List<Member> applicants){
        if(applicants != null){
            return applicants.stream()
                    .map(MemberResponseDto::from)
                    .collect(Collectors.toList());
        }
        else{
            return null;
        }
    }

    private static List<String> tagNames(List<Tag> tags){
        return objectsToString(tags,Tag::getName);
    }

    private static List<String> roleNames(List<Role> roles){
        return objectsToString(roles, Role::getRoleName);
    }

}
