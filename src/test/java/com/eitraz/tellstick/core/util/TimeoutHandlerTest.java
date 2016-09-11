package com.eitraz.tellstick.core.util;

import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class TimeoutHandlerTest {
    public static final String MY_VALUE = "myValue";
    public static final Duration TIMEOUT = Duration.ofMillis(500);

    @Test
    public void testIsReady() throws InterruptedException {
        TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

        assertTrue(timeoutHandler.isReady(MY_VALUE, TIMEOUT));
        assertFalse(timeoutHandler.isReady(MY_VALUE, TIMEOUT));

        Thread.sleep(750);

        assertTrue(timeoutHandler.isReady(MY_VALUE, TIMEOUT));
        assertFalse(timeoutHandler.isReady(MY_VALUE, TIMEOUT));
    }

    @Test
    public void testClean() throws InterruptedException {
        TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>(Duration.ofSeconds(1));

        assertTrue(timeoutHandler.isReady(MY_VALUE, Duration.ofMillis(2)));

        Thread.sleep(2500);

        assertTrue(timeoutHandler.isReady(MY_VALUE, Duration.ofMillis(2)));
    }
}