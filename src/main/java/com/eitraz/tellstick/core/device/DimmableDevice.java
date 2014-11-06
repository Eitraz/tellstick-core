package com.eitraz.tellstick.core.device;

/**
 * Dimmable Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public interface DimmableDevice extends Device {
    void dim(int level) throws DeviceException;

    /**
     * On (max level)
     *
     * @throws com.eitraz.tellstick.core.device.DeviceException
     */
    void on() throws DeviceException;

    /**
     * Off (min level)
     *
     * @throws com.eitraz.tellstick.core.device.DeviceException
     */
    void off() throws DeviceException;

    /**
     * @return true if device is on or dim level is greater than zero
     */
    boolean isOn();
}
