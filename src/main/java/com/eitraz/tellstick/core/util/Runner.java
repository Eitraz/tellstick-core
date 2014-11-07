package com.eitraz.tellstick.core.util;

import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Runner
 * <p/>
 * Created by Petter Alstermark on 2014-11-07.
 */
public class Runner {
    private static final Logger logger = Logger.getLogger(Runner.class);

    private final BlockingQueue<Runnable> runnables = new LinkedBlockingDeque<>();
    private RunnerThread thread;

    /**
     * Create new runner, but don't start it
     */
    public Runner() {
        this(false);
    }

    /**
     * @param start start if true
     */
    public Runner(boolean start) {
        if (start)
            start();
    }

    /**
     * Start (and clear)
     */
    public void start() {
        thread = new RunnerThread();
        thread.start();

        // Clear all current runnables
        runnables.clear();
    }

    /**
     * Stop (and clear)
     */
    public void stop() {
        // Stop thread
        if (thread != null) {
            RunnerThread currentThread = thread;
            thread = null;
            currentThread.interrupt();
        }

        // Clear all current runnables
        runnables.clear();
    }

    /**
     * @param runnable runnable to be run
     */
    public void offer(Runnable runnable) {
        if (logger.isTraceEnabled())
            logger.trace(String.format("Runnable added: %s", runnable.toString()));

        // Offer
        runnables.offer(runnable);
    }

    /**
     * Runner Thread
     */
    private class RunnerThread extends Thread {
        @Override
        public void run() {
            while (thread == this) {
                try {
                    // Wait for new runnable
                    Runnable runnable = runnables.take();

                    // Run
                    if (runnable != null && thread == this) {
                        if (logger.isTraceEnabled())
                            logger.trace(String.format("Running: %s", runnable.toString()));

                        // Run
                        runnable.run();
                    }
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }
}
