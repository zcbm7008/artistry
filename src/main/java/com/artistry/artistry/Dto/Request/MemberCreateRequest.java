
package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.member.Member;
import com.artistry.artistry.Domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateRequest {

    @NonNull
    private String nickName;
    private String email;
    private String iconUrl;

    public Member toEntity() {
        return new Member(nickName,email,iconUrl);
    }
}
