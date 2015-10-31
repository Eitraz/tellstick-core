package com.eitraz.tellstick.core.device;

/**
 * Bell Device
 */
public interface BellDevice extends Device {
    /**
     * Bell
     */
    void bell() throws DeviceException;
}
