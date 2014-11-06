package com.eitraz.tellstick.manager;

import com.eitraz.tellstick.core.device.*;
import org.apache.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Device Manager
 *
 * @author Petter Alstermark
 */
public class DeviceManager {
    private static final Logger logger = Logger.getLogger(DeviceManager.class);

    private final SortedSet<DeviceAction> actions = new TreeSet<>();

    public int retryDelay = 2000;
    public int retries = 3;

    private DeviceActionThread actionThread;

    /**
     * Start
     */
    public void start() {
        // Start new Device Action Thread
        if (actionThread == null) {
            actionThread = new DeviceActionThread();
            actionThread.start();
        }
    }

    /**
     * Stop
     */
    public void stop() {
        if (actionThread != null) {
            DeviceActionThread thread = actionThread;
            actionThread = null;

            // Interrupt
            thread.interrupt();

            try {
                thread.join(10000);
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for DeviceActionThread", e);
            }
        }
    }

    /**
     * @return the retries
     */
    public int getRetries() {
        return retries;
    }

    /**
     * @param retries the retries to set
     */
    public void setRetries(int retries) {
        this.retries = retries;
    }

    /**
     * @return the retryDelay
     */
    public int getRetryDelay() {
        return retryDelay;
    }

    /**
     * @param retryDelay the retryDelay to set
     */
    public void setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
    }

    /**
     * Set device on/off
     *
     * @param device device
     * @param on     true true of on, false for off
     */
    public void set(OnOffDevice device, boolean on) {
        logger.trace(device + ", " + on);
        addAction(new DeviceAction(device, on, retries));
    }

    /**
     * Set device on/off
     *
     * @param device device
     * @param on     true true of on, false for off
     */
    public void set(GroupDevice device, boolean on) {
        logger.trace(device + ", " + on);
        addAction(new DeviceAction(device, on, retries));
    }

    /**
     * Set dim level
     *
     * @param device device
     * @param level  dim level
     */
    public void set(DimmableDevice device, int level) {
        logger.trace(device + ", " + level);
        addAction(new DeviceAction(device, level, retries));
    }

    /**
     * Set device on/off
     *
     * @param device device
     * @param on     true true of on, false for off
     */
    public void set(DimmableDevice device, boolean on) {
        logger.trace(device + ", " + on);
        addAction(new DeviceAction(device, on, retries));
    }

    /**
     * Set device up/down
     *
     * @param device device
     * @param up     true for up, false to for down
     */
    public void set(UpDownDevice device, boolean up) {
        logger.trace(device + ", " + up);
        addAction(new DeviceAction(device, up, retries));
    }

    /**
     * Stop up/down device
     *
     * @param device device
     */
    public void stop(UpDownDevice device) {
        logger.trace(device);
        addAction(new DeviceAction(device, null, retries));
    }

    /**
     * Bell device
     *
     * @param device device
     */
    public void bell(BellDevice device) {
        logger.trace(device);
        addAction(new DeviceAction(device, null, 0));
    }

    /**
     * Execute scene device
     *
     * @param device device
     */
    public void execute(SceneDevice device) {
        logger.trace(device);
        addAction(new DeviceAction(device, null, 0));
    }

    /**
     * Add action
     *
     * @param action device action
     */
    protected void addAction(DeviceAction action) {
        synchronized (actions) {
            logger.trace("Add action: " + action);
            actions.add(action);
            actions.notify();
        }
    }

    /**
     * Device Action Thread
     *
     * @author Petter Alstermark
     */
    private class DeviceActionThread extends Thread {
        private static final int ACTION_DELAY = 500;

        @Override
        public void run() {
            while (actionThread == this) {
                DeviceAction action = null;
                synchronized (actions) {
                    // Wait for action
                    if (actions.isEmpty()) {
                        try {
                            actions.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }

                    // Get action
                    try {
                        action = actions.first();

                        // Remove from set
                        actions.remove(action);
                    } catch (NoSuchElementException ignored) {
                    }
                }

                // Skip ahead if action is null
                if (action == null)
                    continue;

                // Always add a minor delay to allow other signals to arrive
                try {
                    Thread.sleep(ACTION_DELAY);
                } catch (InterruptedException ignored) {
                }

                // Wait until it's time to run
                while (System.currentTimeMillis() <= action.getTime()) {
                    // skip time
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }

                // Do
                try {
                    logger.trace("Handle device action: " + action);
                    DeviceActionHandler.handleDeviceAction(action);
                } catch (Exception e) {
                    logger.error("Device Action failed", e);
                }

                // Add to set
                if (action.getCounter() > 0) {
                    action.setCounter(action.getCounter() - 1);
                    action.setTime(System.currentTimeMillis() + retryDelay + new Random().nextInt(retryDelay / 10));

                    // Add
                    synchronized (actions) {
                        if (!actions.contains(action))
                            actions.add(action);
                    }
                }
            }
        }

    }


}
