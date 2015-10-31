package com.eitraz.tellstick.core.device;

/**
 * On/Off Device
 */
public interface OnOffDevice extends Device {
    /**
     * Turn on
     */
    void on() throws DeviceException;

    /**
     * Turn Off
     */
    void off() throws DeviceException;

    /**
     * @return true if device is on
     */
    boolean isOn();
}
