package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

/**
 * Device
 * <p/>
 * Created by Petter Alstermark on 2014-11-06.
 */
public interface Device extends Comparable<Device> {
    /**
     * @return the deviceId
     */
    public int getDeviceId();

    /**
     * @return the name
     */
    public String getName();

    /**
     * @return the model
     */
    public String getModel();

    /**
     * @return the protocol
     */
    public String getProtocol();

    /**
     * @return the deviceType
     */
    public int getDeviceType();

    /**
     * @return status
     */
    public int getStatus();

    /**
     * Get Library
     *
     * @return library
     */
    public TellstickCoreLibrary getLibrary();

    /**
     * Get Device Handler
     *
     * @return device handler
     */
    public DeviceHandler getDeviceHandler();
}
