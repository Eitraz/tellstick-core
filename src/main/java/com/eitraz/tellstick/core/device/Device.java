package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

/**
 * Device
 */
public interface Device extends Comparable<Device> {
    /**
     * @return the deviceId
     */
    int getDeviceId();

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the model
     */
    String getModel();

    /**
     * @return the protocol
     */
    String getProtocol();

    /**
     * @return the deviceType
     */
    int getDeviceType();

    /**
     * @return status
     */
    int getStatus();

    /**
     * Get Library
     *
     * @return library
     */
    TellstickCoreLibrary getLibrary();

    /**
     * Get Device Handler
     *
     * @return device handler
     */
    DeviceHandler getDeviceHandler();
}
