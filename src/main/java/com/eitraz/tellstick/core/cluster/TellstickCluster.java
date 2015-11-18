package com.eitraz.tellstick.core.cluster;

import com.eitraz.library.hazelcast.HazelcastProxy;
import com.eitraz.tellstick.core.Tellstick;
import com.eitraz.tellstick.core.TellstickImpl;
import com.eitraz.tellstick.core.device.Device;
import com.eitraz.tellstick.core.device.DeviceHandler;

public class TellstickCluster extends HazelcastProxy {
    public static final String DEVICE_PREFIX = "device.";
    public static final String TELLSTICK = "tellstick";
    public static final String DEVICE_HANDLER = "deviceHandler";

    private TellstickImpl tellstick;

    public TellstickCluster() {
        super("tellstick");
    }

    @Override
    protected String createObjectReference(Object object) {
        // Device
        if (object instanceof Device) {
            return String.format("%s%s", DEVICE_PREFIX, ((Device) object).getName());
        }
        // Tellstick
        else if (object instanceof Tellstick) {
            return TELLSTICK;
        }
        // Device Handler
        else if (object instanceof DeviceHandler) {
            return DEVICE_HANDLER;
        }
        // Unhandled
        else {
            throw new IllegalArgumentException("Can't create reference for object");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <O> O createObjectFromReference(String reference) {
        // Device
        if (reference.startsWith(DEVICE_PREFIX)) {
            final String deviceName = reference.substring(DEVICE_PREFIX.length());
            return (O) tellstick.getDeviceByName(deviceName);
        }
        // Tellstick
        else if (reference.equals(TELLSTICK)) {
            return (O) tellstick;
        }
        // Device Handler
        else if (reference.equals(DEVICE_HANDLER)) {
            return (O) tellstick.getDeviceHandler();
        }
        // Unhandled
        else {
            throw new IllegalArgumentException(String.format("Can't create object for reference '%s'", reference));
        }
    }

    public TellstickImpl getTellstick() {
        return tellstick;
    }

    public Tellstick getProxiedTellstick() {
        return proxy(tellstick, TellstickImpl.class);
    }

    public DeviceHandler getProxiedDeviceHandler() {
        return proxy(tellstick.getDeviceHandler(), DeviceHandler.class);
    }

    public <T extends Device> T getProxiedDeviceByName(String name, Class<T> type) {
        return proxy(tellstick.getDeviceByName(name), type);
    }

    @Override
    public void doStart() {
        super.doStart();

        if (tellstick == null) {
            tellstick = new TellstickImpl();
            tellstick.start();
        }
    }

    @Override
    public void doStop() {
        super.doStop();

        if (tellstick != null) {
            tellstick.stop();
            tellstick = null;
        }
    }
}
