package com.eitraz.tellstick.core.device;

/**
 * Test Bell Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-07.
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
