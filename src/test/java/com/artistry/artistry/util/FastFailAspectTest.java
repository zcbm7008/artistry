package com.artistry.artistry.util;

import com.artistry.artistry.Service.RequestCounterService;
import com.artistry.artistry.restdocs.ApiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
public class FastFailAspectTest extends ApiTest {

    @Autowired
    private RequestCounterService requestCounterService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation){
        super.setUp(restDocumentation);
        requestCounterService.reset(); // 요청 카운터 초기화 메서드 추가 필요
    }

    @Test
    public void testFastFailAspect_withinLimit() {
        given().when().get("/api/tags")
                .then()
                .statusCode(200);

        assertThat(requestCounterService.getActiveRequests()).isEqualTo(0);
    }

    @Test
    public void testFastFailAspect_overLimit() throws InterruptedException {
        // MAX_ACTIVE_REQUESTS를 초과하도록 요청을 반복
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                given().when().get("/api/fake");
            }).start();
        }

        // 약간의 대기 시간 추가
        Thread.sleep(2000);

        // activeRequests 값 확인
        System.out.println("Active requests before final request: " + requestCounterService.getActiveRequests());

        given().when().get("/api/tags")
                .then()
                .statusCode(503)
                .body(equalTo("Service is overloaded"));

        System.out.println("Active requests after final request: " + requestCounterService.getActiveRequests());
    }
}
