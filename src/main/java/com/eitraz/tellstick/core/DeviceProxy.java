package com.eitraz.tellstick.core;

import com.eitraz.tellstick.core.device.Device;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Device Proxy
 * <p/>
 * Add a "redelivery" to all void method calls
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public final class DeviceProxy {
    private static final Logger logger = Logger.getLogger(DeviceProxy.class);

    private static DeviceProxy instance;

    private final Map<Integer, DeviceMethodCall> deviceMethodCallsMap = new LinkedHashMap<>();
    private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

    private int tries = 3;
    private long callDelay = 500;

    private DeviceMethodCaller thread;

    /**
     * Start thread
     */
    protected DeviceProxy() {
        thread = new DeviceMethodCaller();
        thread.start();
    }

    /**
     * @return DeviceProxy
     */
    public static DeviceProxy getInstance() {
        // Create new instance
        if (instance == null) {
            instance = new DeviceProxy();
        }
        return instance;
    }

    /**
     * @return number of tries for each call
     */
    public int getTries() {
        return tries;
    }

    /**
     * @param tries number of tries for each call
     */
    public void setTries(int tries) {
        this.tries = tries;
    }

    /**
     * @return delay between calls
     */
    public long getCallDelay() {
        return callDelay;
    }

    /**
     * @param callDelay delay between calls
     */
    public void setCallDelay(long callDelay) {
        this.callDelay = callDelay;
    }

    /**
     * @param deviceId device ID
     * @return DeviceMethodCall
     */
    private DeviceMethodCall getDeviceMethodCall(int deviceId) {
        synchronized (deviceMethodCallsMap) {
            return deviceMethodCallsMap.remove(deviceId);
        }
    }

    /**
     * @param deviceMethodCall method call
     * @param override         if true, override any existing DeviceMethodCall with the same device ID
     */
    private void queue(DeviceMethodCall deviceMethodCall, boolean override) {
        synchronized (deviceMethodCallsMap) {
            // Add Device Method Call
            if (override || !deviceMethodCallsMap.containsKey(deviceMethodCall.getDeviceId())) {
                deviceMethodCallsMap.put(deviceMethodCall.getDeviceId(), deviceMethodCall);
            }
        }

        // Queue for run
        if (!queue.contains(deviceMethodCall.getDeviceId())) {
            queue.offer(deviceMethodCall.getDeviceId());
        }
    }

    /**
     * @param device      device
     * @param deviceClass device class
     * @param <T>         device type
     * @return proxied device
     */
    @SuppressWarnings("unchecked")
    public <T extends Device> T doProxy(final T device, Class<T> deviceClass) {
        return (T) Proxy.newProxyInstance(DeviceProxy.class.getClassLoader(), new Class[]{deviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // Do tries
                if (method.getReturnType().equals(Void.TYPE)) {
                    queue(new DeviceMethodCall(device, method, args), true);
                    return Void.TYPE;
                }
                // Call once and return result
                else {
                    return method.invoke(device, args);
                }
            }
        });
    }

    /**
     * @param device      device
     * @param deviceClass device class
     * @param <T>         device type
     * @return proxied device
     */
    public static <T extends Device> T proxy(T device, Class<T> deviceClass) {
        return getInstance().doProxy(device, deviceClass);
    }

    /**
     * Thread to do the device method calls
     */
    private class DeviceMethodCaller extends Thread {
        @Override
        public void run() {
            while (thread == this) {
                try {
                    Integer deviceId = queue.take();

                    // Skipp null
                    if (deviceId == null)
                        continue;

                    // Get Device Method Call
                    DeviceMethodCall deviceMethodCall = getDeviceMethodCall(deviceId);

                    // Skipp null
                    if (deviceMethodCall == null)
                        continue;

                    // Call device method
                    deviceMethodCall.call();

                    // Add for new call try
                    if (deviceMethodCall.getTries() < tries) {
                        queue(deviceMethodCall, false);
                    }

                    if (logger.isTraceEnabled())
                        logger.trace(String.format("Delaying %d milliseconds", getCallDelay()));

                    // Delay
                    Thread.sleep(getCallDelay());
                } catch (InterruptedException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Device Cethod Call
     */
    private class DeviceMethodCall {
        private int tries;
        private Device device;
        private Method method;
        private Object[] args;

        /**
         * @param device device
         * @param method method
         * @param args   arguments
         */
        private DeviceMethodCall(Device device, Method method, Object[] args) {
            this.device = device;
            this.method = method;
            this.args = args;

            this.tries = 0;
        }

        /**
         * Call
         *
         * @throws InvocationTargetException
         * @throws IllegalAccessException
         */
        public void call() throws InvocationTargetException, IllegalAccessException {
            tries++;

            if (logger.isDebugEnabled())
                logger.debug(String.format("call #%d to '%s' (%d) method '%s'", tries, device.getName(), device.getDeviceId(), method.getName()));

            method.invoke(device, args);
        }

        /**
         * @return device ID
         */
        public int getDeviceId() {
            return device.getDeviceId();
        }

        /**
         * @return number of tries performed
         */
        public int getTries() {
            return tries;
        }
    }

}
