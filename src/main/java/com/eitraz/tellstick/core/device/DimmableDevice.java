package com.eitraz.tellstick.core.device;

/**
 * Dimmable Device
 */
public interface DimmableDevice extends Device {
    void dim(int level) throws DeviceException;

    /**
     * On (max level)
     */
    void on() throws DeviceException;

    /**
     * Off (min level)
     */
    void off() throws DeviceException;

    /**
     * @return true if device is on or dim level is greater than zero
     */
    boolean isOn();
}
