
package com.artistry.artistry.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthMemberRequest {

    @NonNull
    private String nickName;
    private String email;
    private String iconUrl;
}
