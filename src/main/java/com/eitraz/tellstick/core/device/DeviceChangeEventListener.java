package com.eitraz.tellstick.core.device;


import com.eitraz.tellstick.core.TellstickCoreLibrary;

@SuppressWarnings("WeakerAccess")
public interface DeviceChangeEventListener {
    /**
     * Device Changed
     *
     * @param deviceId   device ID
     * @param device     device
     * @param changeType Indicates what has changed  (e.g {@link TellstickCoreLibrary#TELLSTICK_CHANGE_NAME}, {@link TellstickCoreLibrary#TELLSTICK_CHANGE_PROTOCOL}, {@link TellstickCoreLibrary#TELLSTICK_CHANGE_MODEL} or {@link TellstickCoreLibrary#TELLSTICK_CHANGE_METHOD})
     */
    void deviceChanged(int deviceId, Device device, int changeType);

    /**
     * Device Added
     *
     * @param deviceId device ID
     * @param device   device
     */
    void deviceAdded(int deviceId, Device device);

    /**
     * Device Removed
     *
     * @param deviceId device ID
     */
    void deviceRemoved(int deviceId);
}
