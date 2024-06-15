package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String nickName;
    private String email;
    private String iconUrl;
    private String bio;
    private List<LinkResponse> links;

    public static MemberResponse from(Member member){
        return new MemberResponse(
                member.getId(),
                member.getNickname(),
                member.getEmail(),
                member.getIconUrl(),
                member.getBio(),
                getLinks(member)
        );
    }

    public static List<LinkResponse> getLinks(Member member){
        if (member.getMemberLinks() == null){
            return new ArrayList<>();
        }

        return member.getMemberLinks().stream().map(LinkResponse::from).collect(Collectors.toList());
    }
}
