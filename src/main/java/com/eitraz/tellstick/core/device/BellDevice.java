package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

public class BellDevice extends Device {

    public BellDevice(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    /**
     * Bell
     *
     * @throws DeviceException
     */
    public void bell() throws DeviceException {
        logger.debug("BELL " + toString());

        int status = getLibrary().tdBell(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

}
