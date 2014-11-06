package com.eitraz.tellstick.core.device.impl;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.device.AbstractDevice;
import com.eitraz.tellstick.core.device.DeviceException;
import com.eitraz.tellstick.core.device.DeviceHandler;
import com.eitraz.tellstick.core.device.UpDownDevice;

public class UpDownDeviceImpl extends AbstractDevice implements UpDownDevice {

    public UpDownDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    @Override
    public void up() throws DeviceException {
        logger.debug("UP " + toString());

        int status = getLibrary().tdUp(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    @Override
    public void down() throws DeviceException {
        logger.debug("DOWN " + toString());

        int status = getLibrary().tdDown(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

    @Override
    public void stop() throws DeviceException {
        logger.debug("STOP " + toString());

        int status = getLibrary().tdStop(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

}
