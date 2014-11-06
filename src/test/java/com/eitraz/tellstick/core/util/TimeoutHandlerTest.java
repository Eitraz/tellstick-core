package com.eitraz.tellstick.core.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeoutHandlerTest {
    public static final String MY_VALUE = "myValue";

    @Test
    public void testIsReady() throws InterruptedException {
        TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>(500);

        assertTrue(timeoutHandler.isReady(MY_VALUE));
        assertFalse(timeoutHandler.isReady(MY_VALUE));

        Thread.sleep(750);

        assertTrue(timeoutHandler.isReady(MY_VALUE));
        assertFalse(timeoutHandler.isReady(MY_VALUE));
    }

    @Test
    public void testClean() throws InterruptedException {
        TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>(2);

        assertTrue(timeoutHandler.isReady(MY_VALUE));

        Thread.sleep(750);

        assertTrue(timeoutHandler.isReady(MY_VALUE));
    }
}