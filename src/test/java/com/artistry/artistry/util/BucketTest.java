package com.artistry.artistry.util;

import com.artistry.artistry.Service.RequestCounterService;
import com.artistry.artistry.restdocs.ApiTest;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.time.Duration;

public class BucketTest extends ApiTest {
    @Autowired
    Bucket bucket;
    @Autowired
    RequestCounterService requestCounterService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation){
        super.setUp(restDocumentation);
    }


    @Test
    void testBucket4j() throws InterruptedException {
        System.out.println("Initial tokens: " + bucket.getAvailableTokens());
        bucket.tryConsumeAsMuchAsPossible();
        System.out.println("Tokens after tryConsumeAsMuchAsPossible: " + bucket.getAvailableTokens());
        bucket.tryConsume(25);
        System.out.println("Tokens after tryConsume(25): " + bucket.getAvailableTokens());

        given().when().get("/api/tags")
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value());
    }
}
