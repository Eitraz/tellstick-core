package com.eitraz.tellstick.core.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class TimeoutHandler<T> {
	private static final long DEFAULT_TIMEOUT = 1000;

	private final Map<T, Long> cache = new ConcurrentHashMap<T, Long>();

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
	 * @param string
	 * @return true if value is timed out
	 */
	public synchronized boolean isReady(T value) {
		// Clean up timed out values
		clean();

		// Get timeout value
		Long timeout = cache.get(value);

		// Add timeout for value
		if (timeout == null) {
			cache.put(value, System.currentTimeMillis() + getTimeout());
		}

		// Return true if value is timed out
		return timeout == null;
	}

	/**
	 * Clean
	 */
	private void clean() {
		long time = System.currentTimeMillis();
		for (Entry<T, Long> entry : cache.entrySet()) {
			// Remove if timed out
			if (entry.getValue() <= time) {
				cache.remove(entry.getKey());
			}
		}
	}

}
