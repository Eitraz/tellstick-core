package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

/**
 * Abstract Test Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-07.
 */
public abstract class AbstractTestDevice implements Device {
    private int deviceId;
    private String name;

    protected int counter;

    public AbstractTestDevice(int deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
    }

    @Override
    public int getDeviceId() {
        return deviceId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getModel() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public int getDeviceType() {
        return 0;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public TellstickCoreLibrary getLibrary() {
        return null;
    }

    @Override
    public DeviceHandler getDeviceHandler() {
        return null;
    }

    @Override
    public int compareTo(Device o) {
        return 0;
    }

    /**
     * @return counter value
     */
    public int getCounter() {
        return counter;
    }

}
