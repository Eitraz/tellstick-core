package com.eitraz.tellstick.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class RunnerTest {
    private Runner runner;
    private int counter;

    @Before
    public void before() {
        runner = new Runner();
        counter = 0;
    }

    @After
    public void after() {
        // Stop runner
        if (runner != null) {
            runner.stop();
            runner = null;
        }
    }

    @Test
    public void testStart() throws Exception {
        // Add increase counter runnable
        runner.offer(new IncreaseCounter());

        // Nothing should happen
        Thread.sleep(500);
        assertEquals(0, counter);

        // Start (and clear)
        runner.start();

        // Add increase counter runnable
        runner.offer(new IncreaseCounter());

        // Counter should be increased by one
        await().until(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return counter;
            }
        }, equalTo(1));
    }

    @Test
    public void testStop() throws Exception {
        // Start
        runner.start();

        // Add increase counter runnable
        runner.offer(new IncreaseCounter());

        // Counter should be increased by one
        await().until(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return counter;
            }
        }, equalTo(1));

        // Stop (clear)
        runner.stop();

        // Add increase counter runnable
        runner.offer(new IncreaseCounter());

        // Nothing should happen
        Thread.sleep(500);
        assertEquals(1, counter);

        // Start again (and clear)
        runner.start();

        // Add increase counter runnable
        runner.offer(new IncreaseCounter());

        // Counter should be increased by one
        await().until(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return counter;
            }
        }, equalTo(2));
    }

    @Test
    public void testOffer() throws Exception {
        // Start
        runner.start();

        // Add increase counter runnable
        for (int i = 0; i < 5; i++) {
            runner.offer(new IncreaseCounter());
        }

        // Counter should be increased by 5
        await().until(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return counter;
            }
        }, equalTo(5));
    }

    /**
     * Test runnable
     */
    private class IncreaseCounter implements Runnable {
        @Override
        public void run() {
            counter++;
        }
    }
}