package com.artistry.artistry.Controller;

import com.artistry.artistry.Service.OAuthService;
import com.artistry.artistry.auth.oauth.SocialType;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OAuthService oAuthService;

    @GetMapping("/oauth/{provider}/login")
    public ResponseEntity<Void> login(@PathVariable("provider") String provider) {
        SocialType socialType = oAuthService.getSocialType(provider);
        String url = oAuthService.generateLoginLink(socialType);
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();
        return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
    }

    @GetMapping("/code/{provider}")
    public ResponseEntity<AccessTokenResponse> redirect(@PathVariable("provider") String provider, @RequestParam final String code) throws IOException {
        SocialType socialType = oAuthService.getSocialType(provider);
        final AccessTokenResponse response = oAuthService.createMemberAccessToken(socialType,code);

        return ResponseEntity.ok(response);
    }


}
