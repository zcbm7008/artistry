package com.artistry.artistry.Dto.Response;

import com.artistry.artistry.Domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String name;

    public static MemberResponseDto from(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getNickname())
                .build();
    }
}
