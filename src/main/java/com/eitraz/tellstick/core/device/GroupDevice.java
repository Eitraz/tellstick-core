package com.eitraz.tellstick.core.device;

import java.util.List;

/**
 * Group Device
 */
public interface GroupDevice extends Device {
    /**
     * Get Devices
     *
     * @return devices
     */
    List<Device> getDevices();

    /**
     * Turn on
     */
    void on();

    /**
     * Turn off
     */
    void off();
}
