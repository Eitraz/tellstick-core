package com.eitraz.tellstick.core.device;

/**
 * Bell Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public interface BellDevice extends Device {
    /**
     * Bell
     *
     * @throws com.eitraz.tellstick.core.device.DeviceException
     */
    void bell() throws DeviceException;
}
