package com.eitraz.tellstick.core;

import com.eitraz.tellstick.core.device.Device;
import com.eitraz.tellstick.core.device.DeviceHandler;
import com.eitraz.tellstick.core.rawdevice.RawDeviceHandler;
import com.eitraz.tellstick.core.sensor.SensorHandler;

public interface Tellstick {
    DeviceHandler getDeviceHandler();

    RawDeviceHandler getRawDeviceHandler();

    TellstickCoreLibrary getLibrary();

    SensorHandler getSensorHandler();

    <T extends Device> T getDeviceByName(String name);
}
