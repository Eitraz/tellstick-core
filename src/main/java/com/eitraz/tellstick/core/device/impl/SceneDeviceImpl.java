package com.eitraz.tellstick.core.device.impl;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.device.AbstractDevice;
import com.eitraz.tellstick.core.device.DeviceException;
import com.eitraz.tellstick.core.device.DeviceHandler;
import com.eitraz.tellstick.core.device.SceneDevice;


public class SceneDeviceImpl extends AbstractDevice implements SceneDevice {

    public SceneDeviceImpl(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);
    }

    @Override
    public void execute() throws DeviceException {
        logger.debug("EXECUTE " + toString());

        int status = getLibrary().tdExecute(getDeviceId());

        if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
            throw new DeviceException(this, status);
    }

}
