package com.artistry.artistry.auth.oauth;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OAuthMemberResponse {
    private String nickName;
    private String email;
    private String profileImageUrl;
}
