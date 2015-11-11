package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickException;

import java.util.List;
import java.util.Map;

public interface DeviceHandler {
    /**
     * @param listener device event listener
     */
    void addDeviceEventListener(DeviceEventListener listener);

    /**
     * @param listener device event listener
     */
    void removeDeviceEventListener(DeviceEventListener listener);

    void start();

    void stop();

    /**
     * @return devices
     */
    List<Device> getDevices();

    /**
     * @param deviceId device ID
     * @return device
     * @throws DeviceNotSupportedException
     */
    Device getDevice(int deviceId) throws DeviceNotSupportedException;

    /**
     * @param name       name
     * @param model      model
     * @param protocol   protocol
     * @param parameters parameters
     * @return device
     * @throws TellstickException
     * @throws DeviceNotSupportedException
     */
    Device createDevice(String name, String model, String protocol, Map<String, String> parameters) throws TellstickException, DeviceNotSupportedException;

    /**
     * @param deviceId device ID
     * @return true if device was removed
     */
    boolean removeDevice(int deviceId);

    /**
     * @param device device
     * @return true if device was removed
     */
    default boolean removeDevice(Device device) {
        return removeDevice(device.getDeviceId());
    }

    /**
     * @return the supportedMethods
     */
    int getSupportedMethods();

    /**
     * @return the library
     */
    TellstickCoreLibrary getLibrary();
}
