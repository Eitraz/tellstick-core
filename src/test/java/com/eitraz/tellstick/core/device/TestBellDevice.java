package com.eitraz.tellstick.core.device;

/**
 * Test Bell Device
 */
public class TestBellDevice extends AbstractTestDevice implements BellDevice {

    public TestBellDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public void bell() throws DeviceException {
        counter++;
    }
}
