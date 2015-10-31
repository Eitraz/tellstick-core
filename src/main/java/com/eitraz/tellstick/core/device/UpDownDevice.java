package com.eitraz.tellstick.core.device;

/**
 * Up/Down Device
 */
public interface UpDownDevice extends Device {
    /**
     * Up
     */
    void up() throws DeviceException;

    /**
     * Down
     */
    void down() throws DeviceException;

    /**
     * Stop
     */
    void stop() throws DeviceException;
}
