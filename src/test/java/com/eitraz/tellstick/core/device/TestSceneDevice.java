package com.eitraz.tellstick.core.device;

/**
 * Test Scene Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-07.
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
