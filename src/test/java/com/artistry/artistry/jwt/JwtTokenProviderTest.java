package com.artistry.artistry.jwt;

import com.artistry.artistry.auth.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String secretKey = "mysecretkeymysecretkeymysecretkeymysecretkey";
    private final long validityInMilliseconds = 3600000; // 1 hour

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @DisplayName("Jwt 토큰을 생성한다.")
    @Test
    public void testGenerateToken() {
        String key = "testkey";
        String value = "testvalue";

        String token = jwtTokenProvider.generateToken(key,value);

        assertNotNull(token);
        assertTrue(token.length() > 0);

    }

    @DisplayName("Jwt 토큰의 값을 추출한다.")
    @Test
    public void testExtractValueFromToken(){
        String key = "testkey";
        String expectedValue = "testvalue";
        String token = jwtTokenProvider.generateToken(key,expectedValue);
        String value = jwtTokenProvider.extractValueFromToken(key,token);
        assertThat(expectedValue).isEqualTo(value);
    }

    @DisplayName("Email Jwt 토큰을 생성한다.")
    @Test
    public void TestGenereateEmailToken(){
        String email = "a@a.com";
        String emailToken = jwtTokenProvider.generateEmailToken(email);
    }

    @DisplayName("Email Jwt 토큰의 값을 추출한다")
    @Test
    public void TestExtractEmailFormToken(){
        String expectedEmail = "a@a.com";
        String emailToken = jwtTokenProvider.generateEmailToken(expectedEmail);
        String email = jwtTokenProvider.extractEmailFromToken(emailToken);

        assertThat(expectedEmail).isEqualTo(email);
    }

}
