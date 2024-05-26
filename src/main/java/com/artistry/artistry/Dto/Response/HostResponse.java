package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HostResponse {
    private Long id;
    private String nickName;

    public static HostResponse from(Member member){
        return new HostResponse(member.getId(), member.getNickname());
    }
}
