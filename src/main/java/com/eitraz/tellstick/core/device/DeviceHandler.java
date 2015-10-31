package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickException;

import java.util.List;
import java.util.Map;

/**
 * Created by petter on 2015-10-31.
 */
public interface DeviceHandler {
    void addDeviceEventListener(DeviceEventListener listener);

    void removeDeviceEventListener(DeviceEventListener listener);

    void start();

    void stop();

    List<Device> getDevices();

    Device getDevice(int deviceId) throws DeviceNotSupportedException;

    Device createDevice(String name, String model, String protocol, Map<String, String> parameters) throws TellstickException, DeviceNotSupportedException;

    boolean removeDevice(int deviceId);

    boolean removeDevice(Device device);

    int getSupportedMethods();

    TellstickCoreLibrary getLibrary();
}
