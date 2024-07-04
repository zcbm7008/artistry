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
        bucket.tryConsumeAsMuchAsPossible();
        bucket.tryConsume(1);
        given().when().get("/api/tags")
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value());

    }
}
