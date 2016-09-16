package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

/**
 * Up/Down Device
 */
public class UpDownDevice extends Device {
    public UpDownDevice(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    public void up() throws DeviceException {
        logger.info("UP " + toString());

        int status = getLibrary().tdUp(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    public void down() throws DeviceException {
        logger.info("DOWN " + toString());

        int status = getLibrary().tdDown(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    public void stop() throws DeviceException {
        logger.info("STOP " + toString());

        int status = getLibrary().tdStop(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }
}
