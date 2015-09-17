package com.eitraz.tellstick.core.device;

/**
 * Test Dimmable Device
 */
public class TestDimmableDevice extends AbstractTestDevice implements DimmableDevice {
    public TestDimmableDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public void dim(int level) throws DeviceException {
        counter = level;
    }

    @Override
    public void on() throws DeviceException {
        counter++;
    }

    @Override
    public void off() throws DeviceException {
        counter--;
    }

    @Override
    public boolean isOn() {
        return false;
    }
}
