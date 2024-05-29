package com.artistry.artistry.Controller;

import com.artistry.artistry.Dto.Response.LoginUrlResponse;
import com.artistry.artistry.auth.oauth.OAuthService;
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

    @GetMapping("/oauth/google/login")
    public ResponseEntity<LoginUrlResponse> googleLogin() {
        String url =oAuthService.generateLoginLink();
        return ResponseEntity.ok(new LoginUrlResponse(url));
    }

    @GetMapping("/code/google")
    public void googleRedirect(final HttpServletResponse response, @RequestParam final String code) throws IOException {
        final AccessTokenResponse tokenResponse = oAuthService.createToken(code);
        final String url = "/login" + "?accessToken=" + tokenResponse.getAccessToken();

        response.sendRedirect(url);
    }

}
