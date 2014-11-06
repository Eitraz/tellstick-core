package com.eitraz.tellstick.core.device;

/**
 * Up/Down Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public interface UpDownDevice extends Device {
    /**
     * Up
     *
     * @throws com.eitraz.tellstick.core.device.DeviceException
     */
    void up() throws DeviceException;

    /**
     * Down
     *
     * @throws com.eitraz.tellstick.core.device.DeviceException
     */
    void down() throws DeviceException;

    /**
     * Stop
     *
     * @throws com.eitraz.tellstick.core.device.DeviceException
     */
    void stop() throws DeviceException;
}
