package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String nickName;
    private String iconUrl;
    private String email;

    public static MemberResponse from(Member member){
        return new MemberResponse(
                member.getId(),
                member.getNickname(),
                member.getIconUrl(),
                member.getEmail());
    }
}
