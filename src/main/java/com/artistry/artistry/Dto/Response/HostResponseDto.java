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
public class HostResponseDto {
    private Long id;
    private String name;

    public static HostResponseDto from(Member host){
        return HostResponseDto.builder()
                .id(host.getId())
                .name(host.getNickname())
                .build();
    }
}
