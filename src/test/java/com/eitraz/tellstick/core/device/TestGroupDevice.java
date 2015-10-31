package com.eitraz.tellstick.core.device;

import java.util.List;

/**
 * Test Group Device
 */
public class TestGroupDevice extends AbstractTestDevice implements GroupDevice {
    public TestGroupDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public List<Device> getDevices() {
        return null;
    }

    @Override
    public void on() {
        counter++;
    }

    @Override
    public void off() {
        counter--;
    }
}
