package com.artistry.artistry.auth;

import com.artistry.artistry.Exceptions.ArtistryInvalidValueException;
import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthContext authContext;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            throw new ArtistryInvalidValueException("인증 정보가 없습니다.");
        }
        Long memberId = jwtTokenProvider.parseId(authHeader);
        authContext.setMemberId(memberId);
        return true;
    }
}
