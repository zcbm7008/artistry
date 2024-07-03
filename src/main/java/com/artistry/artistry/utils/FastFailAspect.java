package com.artistry.artistry.utils;

import com.artistry.artistry.Service.RequestCounterService;
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

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object checkRequestLimit(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("checked");
        if(!requestCounterService.increment()){
            return ResponseEntity.status(503).body("Service is overloaded");
        }

        try {
            return joinPoint.proceed();
        } finally {
            requestCounterService.decrement();
        }
    }


}
