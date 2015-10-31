package com.eitraz.tellstick.core.device;

/**
 * Test On Off Device
 */
public class TestOnOffDevice extends AbstractTestDevice implements OnOffDevice {
    public TestOnOffDevice(int deviceId, String name) {
        super(deviceId, name);
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
