package com.artistry.artistry.auth.oauth;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthMember {
    private final String email;
    private final String displayName;
    private final String profileImageUrl;
}
