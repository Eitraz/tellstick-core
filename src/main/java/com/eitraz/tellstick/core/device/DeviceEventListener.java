package com.eitraz.tellstick.core.device;


public interface DeviceEventListener {
    /**
     * Device Changed
     *
     * @param deviceId device ID
     * @param device device
     */
    void deviceChanged(int deviceId, Device device);

    /**
     * Device Added
     *
     * @param deviceId device ID
     * @param device device
     */
    void deviceAdded(int deviceId, Device device);

    /**
     * Device Removed
     *
     * @param deviceId device ID
     */
    void deviceRemoved(int deviceId);
}
