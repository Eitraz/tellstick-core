package com.eitraz.tellstick.core.cluster;

import com.eitraz.tellstick.core.Tellstick;
import com.eitraz.tellstick.core.device.Device;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;

import java.io.Serializable;
import java.lang.reflect.Proxy;

public class TellstickCluster {
    private Tellstick tellstick;
    private ITopic<DeviceAction> deviceActions;

    /**
     * Start
     */
    public void start() {
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
        deviceActions = hazelcast.getTopic("tellstick.device.action");

        if (tellstick != null) {
            tellstick = new Tellstick();
            tellstick.start();
        }
    }

    /**
     * Stop
     */
    public void stop() {
        if (tellstick != null) {
            tellstick.stop();
            tellstick = null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Device> T getDevice(final String deviceName) {
        final T device = (T) tellstick.getDeviceHandler()
                .getDevices().stream()
                .filter(d -> deviceName.equals(d.getName()))
                .findFirst().get();

        return (T) Proxy.newProxyInstance(device.getClass().getClassLoader(), new Class<?>[]{device.getClass()},
                (proxy, method, args) -> {
                    // Send to cluster
                    if (method.getReturnType().equals(Void.TYPE)) {
                        deviceActions.publish(new DeviceAction(device.getName(), method.getName(), args));
                        return Void.TYPE;
                    }
                    // Call once and return result
                    else {
                        return method.invoke(device, args);
                    }
                });
    }

    public class DeviceAction implements Serializable {
        private final String deviceName;
        private final String method;
        private final Object[] args;

        public DeviceAction(String deviceName, String method, Object[] args) {
            this.deviceName = deviceName;
            this.method = method;
            this.args = args;
        }
    }
}
