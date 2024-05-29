package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Response.LoginUrlResponse;
import com.artistry.artistry.auth.oauth.OAuthService;
import com.artistry.artistry.auth.oauth.SocialType;
import com.artistry.artistry.auth.properties.AccessTokenResponse;
import com.artistry.artistry.auth.properties.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OAuthService oAuthService;

    @GetMapping("/oauth/{provider}/login")
    public ResponseEntity<LoginUrlResponse> login(@PathVariable("provider") String provider) {

        String url = oAuthService.generateLoginLink(getSocialType(provider));
        return ResponseEntity.ok(new LoginUrlResponse(url));
    }

    @GetMapping("/code/{provider}")
    public void redirect(final HttpServletResponse response,
                               @PathVariable("provider") String provider,
                               @RequestParam final String code) throws IOException {
        final AccessTokenResponse tokenResponse = oAuthService.createToken(getSocialType(provider),code);
        final String url = "/login" + "?accessToken=" + tokenResponse.getAccessToken();

        response.sendRedirect(url);
    }
    public SocialType getSocialType(String provider) {
        try {
            return SocialType.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("정의되지 않은 소셜 타입입니다.", e);
        }
    }

}
