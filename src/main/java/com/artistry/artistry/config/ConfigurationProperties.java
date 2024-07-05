package com.artistry.artistry.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Component
public class ConfigurationProperties {
    private AtomicInteger capacity = new AtomicInteger(100);
    private AtomicInteger refillAmount = new AtomicInteger(5);
    private AtomicInteger refillDurationMilliSeconds = new AtomicInteger(10);
    private AtomicInteger MAX_ACTIVE_REQUESTS = new AtomicInteger(30);
}
