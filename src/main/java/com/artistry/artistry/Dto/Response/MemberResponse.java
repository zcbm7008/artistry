package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;

    public static MemberResponse from(Member member){
        return new MemberResponse(member.getId(), member.getNickname());
    }
}
