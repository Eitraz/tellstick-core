package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

@SuppressWarnings("WeakerAccess")
public interface DeviceEventListener {
    /**
     * Device event
     *
     * @param deviceId device ID
     * @param device   device
     * @param method   The new device state. Can be {@link TellstickCoreLibrary#TELLSTICK_TURNON}, {@link TellstickCoreLibrary#TELLSTICK_TURNOFF}, etc
     * @param data     If method is {@link TellstickCoreLibrary#TELLSTICK_DIM} this holds the current value as human readable string, example "128" or 50%
     */
    void deviceEvent(int deviceId, Device device, int method, String data);
}
