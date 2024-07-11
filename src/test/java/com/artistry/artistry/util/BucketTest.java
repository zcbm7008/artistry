package com.artistry.artistry.util;

import com.artistry.artistry.Service.RequestCounterService;
import com.artistry.artistry.restdocs.ApiTest;
import com.artistry.artistry.utils.FastFailAspect;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.time.Duration;

public class BucketTest extends ApiTest {
    @Autowired
    FastFailAspect fastFailAspect;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation){
        super.setUp(restDocumentation);
        Bucket bucket = Bucket.builder()
                .addLimit(limit -> limit.capacity(1).refillIntervally(1, Duration.ofDays(10000)))
                .build();

        fastFailAspect.setBucket(bucket);
    }

    @Test
    void testBucket4j() throws InterruptedException {
        fastFailAspect.getBucket().tryConsumeAsMuchAsPossible();
        // API 호출
        given().when().get("/api/tags")
                .then()
                .statusCode(HttpStatus.TOO_MANY_REQUESTS.value());
    }
}
