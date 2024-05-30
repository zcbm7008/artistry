package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Response.LoginUrlResponse;
import com.artistry.artistry.Service.OAuthService;
import com.artistry.artistry.auth.oauth.SocialType;
import com.artistry.artistry.Dto.Response.AccessTokenResponse;
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
        SocialType socialType = oAuthService.getSocialType(provider);
        String url = oAuthService.generateLoginLink(socialType);
        return ResponseEntity.ok(new LoginUrlResponse(url));
    }

    @GetMapping("/code/{provider}")
    public void redirect(final HttpServletResponse response,
                               @PathVariable("provider") String provider,
                               @RequestParam final String code) throws IOException {
        SocialType socialType = oAuthService.getSocialType(provider);
        final AccessTokenResponse tokenResponse = oAuthService.createMemberAccessToken(socialType,code);
        final String url = "/login" + "?accessToken=" + tokenResponse.getAccessToken();

        response.sendRedirect(url);
    }


}
