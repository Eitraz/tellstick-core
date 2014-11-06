package com.eitraz.tellstick.core.device;


public interface DeviceEventListener {
    /**
     * Device Changed
     *
     * @param deviceId device ID
     * @param device device
     */
    public void deviceChanged(int deviceId, Device device);

    /**
     * Device Added
     *
     * @param deviceId device ID
     * @param device device
     */
    public void deviceAdded(int deviceId, Device device);

    /**
     * Device Removed
     *
     * @param deviceId device ID
     */
    public void deviceRemoved(int deviceId);
}
