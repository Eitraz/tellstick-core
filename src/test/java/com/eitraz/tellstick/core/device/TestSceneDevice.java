package com.eitraz.tellstick.core.device;

/**
 * Test Scene Device
 */
public class TestSceneDevice extends AbstractTestDevice implements SceneDevice {
    public TestSceneDevice(int deviceId, String name) {
        super(deviceId, name);
    }

    @Override
    public void execute() throws DeviceException {
        counter++;
    }
}
