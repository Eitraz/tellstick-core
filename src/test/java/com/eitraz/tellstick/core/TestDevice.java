package com.eitraz.tellstick.core;

import com.eitraz.tellstick.core.device.Device;
import com.eitraz.tellstick.core.device.DeviceHandler;

/**
 * Test Device
 */
public class TestDevice implements Device {
    private int counter;
    private int deviceId;
    private String name;

    public TestDevice() {
    }

    public TestDevice(int deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;

        this.counter = 0;
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

    /**
     * Test method
     */
    public void count() {
        counter++;
    }
}
