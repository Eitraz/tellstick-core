package com.eitraz.tellstick.core.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TimeoutHandler<T> {
    public final Duration cacheTimeout;

    private Map<T, LocalDateTime> cache = new ConcurrentHashMap<T, LocalDateTime>();

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TimeoutHandler() {
        this(Duration.ofMinutes(1));
    }

    public TimeoutHandler(Duration cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
        scheduler.scheduleAtFixedRate(() -> clean(), cacheTimeout.getSeconds(), cacheTimeout.getSeconds(), TimeUnit.SECONDS);
    }

    private void clean() {
        LocalDateTime timeout = LocalDateTime.now().minus(cacheTimeout);

        cache.entrySet().stream()
                .filter(e -> e.getValue().isBefore(timeout))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .forEach(k -> {
                    cache.remove(k);
                });
    }

    public boolean isReady(T value, Duration timeout) {
        LocalDateTime lastTime = cache.get(value);
        LocalDateTime now = LocalDateTime.now();

        if (lastTime != null && lastTime.plus(timeout).isAfter(now)) {
            return false;
        }

        cache.put(value, now);

        return true;
    }
}
