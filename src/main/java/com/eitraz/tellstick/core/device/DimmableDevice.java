package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

/**
 * Dimmable Device
 */
public class DimmableDevice extends Device {
    public DimmableDevice(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    public void dim(int level) throws DeviceException {
        logger.info("DIM {} {}", level, toString());

        if (level < 0 || level > 255)
            throw new IllegalArgumentException("Dim level must be between 0 and 255.");

        int status;

        // Turn off
        if (level == 0) {
            status = getLibrary().tdTurnOff(getDeviceId());
        }
        // Set dim level (need to turn on?)
        else {
            status = getLibrary().tdDim(getDeviceId(), level);
        }

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    public void on() throws DeviceException {
        dim(255);
    }

    public void off() throws DeviceException {
        dim(0);
    }

    public boolean isOn() {
        boolean isOn = (getStatus() & TellstickCoreLibrary.TELLSTICK_TURNON) > 0;
        return isOn || ((getStatus() & TellstickCoreLibrary.TELLSTICK_DIM) > 0);
    }

    public boolean isOff() {
        return !isOn();
    }
}
