package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

/**
 * On/Off Device
 */
public class OnOffDevice extends Device {
    public OnOffDevice(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    public void on() throws DeviceException {
        logger.debug("ON " + toString());

        int status = getLibrary().tdTurnOn(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    public void off() throws DeviceException {
        logger.debug("OFF " + toString());

        int status = getLibrary().tdTurnOff(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    public boolean isOn() {
        return (getStatus() & TellstickCoreLibrary.TELLSTICK_TURNON) > 0;
    }
}
