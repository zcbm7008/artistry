
package com.artistry.artistry.Dto.Request;

import com.artistry.artistry.Domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequest {

    @NonNull
    private String nickName;
    private String iconUrl;

}
