package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

/**
 * Scene Device
 */
public class SceneDevice extends Device {
    public SceneDevice(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    public void execute() throws DeviceException {
        logger.info("EXECUTE " + toString());

        int status = getLibrary().tdExecute(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }
}
