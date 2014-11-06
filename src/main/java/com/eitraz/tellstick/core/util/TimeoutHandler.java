package com.eitraz.tellstick.core.util;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


public class TimeoutHandler<T> {
    protected static final Logger logger = Logger.getLogger(TimeoutHandler.class);

    private static final long DEFAULT_TIMEOUT = 1000;

    private static final int MAX_CLEAN_TIMEOUT = 600000;
    private static final long CLEAN_TIMEOUT_MULTIPLIER = 60 * 5;

    private long cacheClearTime = System.currentTimeMillis();
    private final Map<T, Long> cache = new ConcurrentHashMap<>();

    private long timeout;

    public TimeoutHandler() {
        this(DEFAULT_TIMEOUT);
    }

    public TimeoutHandler(long timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * @param value
     * @return true if value is timed out
     */
    public synchronized boolean isReady(T value) {
        // Clean up values left behind
        clean();

        // Get timeout value
        Long timeout = cache.get(value);

        long time = System.currentTimeMillis();

        // Timed out
        if (timeout != null && timeout < time) {
            timeout = null;
        }

        logger.debug(String.format("%s timed out: %s", value, timeout == null));

        // Set timeout value
        cache.put(value, time + getTimeout());

        // Return true if value is timed out
        return timeout == null;
    }

    /**
     * Clean up values left behind
     */
    protected void clean() {
        long time = System.currentTimeMillis();

        // Don't clean to often
        if (cacheClearTime + Math.min(getTimeout() * CLEAN_TIMEOUT_MULTIPLIER, MAX_CLEAN_TIMEOUT) < time) {
            if (logger.isDebugEnabled())
                logger.debug("Cleaning cache (values before clean: " + cache.size());

            for (Entry<T, Long> entry : cache.entrySet()) {
                // Remove if timed out
                if (entry.getValue() <= time) {
                    cache.remove(entry.getKey());
                }
            }

            if (logger.isDebugEnabled())
                logger.debug("Values after clean: " + cache.size());

            cacheClearTime = time;
        }
    }

}
