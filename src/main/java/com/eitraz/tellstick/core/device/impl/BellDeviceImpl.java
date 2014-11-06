package com.eitraz.tellstick.core.device.impl;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.device.AbstractDevice;
import com.eitraz.tellstick.core.device.BellDevice;
import com.eitraz.tellstick.core.device.DeviceException;
import com.eitraz.tellstick.core.device.DeviceHandler;

public class BellDeviceImpl extends AbstractDevice implements BellDevice {

    public BellDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    @Override
    public void bell() throws DeviceException {
        logger.debug("BELL " + toString());

        int status = getLibrary().tdBell(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

}
