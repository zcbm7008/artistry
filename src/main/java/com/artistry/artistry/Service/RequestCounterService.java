package com.artistry.artistry.Service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RequestCounterService {
    private final AtomicInteger activeRequests = new AtomicInteger(0);
    private static final int MAX_ACTIVE_REQUESTS = 30;

    public boolean increment() {
        if (activeRequests.incrementAndGet() > MAX_ACTIVE_REQUESTS){
            activeRequests.decrementAndGet();
            return false;
        }
        return true;
    }

    public void decrement() {
        activeRequests.decrementAndGet();
    }

    public int getActiveRequests() {
        return activeRequests.get();
    }

    public void reset() { activeRequests.set(0);}
}
