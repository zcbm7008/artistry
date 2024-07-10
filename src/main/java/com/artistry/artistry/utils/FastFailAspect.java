package com.artistry.artistry.utils;

import com.artistry.artistry.Exceptions.ArtistryTooManyRequests;
import com.artistry.artistry.Service.RequestCounterService;
import io.github.bucket4j.Bucket;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class FastFailAspect {

    @Autowired
    private RequestCounterService requestCounterService;
    @Autowired
    Bucket bucket;

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object checkRequestLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!tryConsumeToken()) {
           throw new ArtistryTooManyRequests("요청이 너무 많습니다.");
        }

        if (!checkRequestCount()) {
            return ResponseEntity.status(503).body("Service is overloaded");
        }

        try {
            return joinPoint.proceed();
        } finally {
            requestCounterService.decrement();
        }
    }

    private boolean tryConsumeToken() {
        return bucket.tryConsume(1);
    }

    private boolean checkRequestCount() {
        return requestCounterService.increment();
    }
}
