package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Group Device
 */
public class GroupDevice extends Device {
    private final List<Device> devices = new ArrayList<>();

    public GroupDevice(DeviceHandler deviceHandler, int deviceId) {
        super(deviceHandler, deviceId);

        logger.trace("Get methods " + deviceId);
        int methods = getLibrary().tdMethods(deviceId, deviceHandler.getSupportedMethods());

        // Bell
        if ((methods & TellstickCoreLibrary.TELLSTICK_BELL) > 0)
            devices.add(new BellDevice(deviceHandler, deviceId));

        // Dim
        if ((methods & TellstickCoreLibrary.TELLSTICK_DIM) > 0)
            devices.add(new DimmableDevice(deviceHandler, deviceId));

            // On/Off
        else if ((methods & TellstickCoreLibrary.TELLSTICK_TURNON) > 0 && (methods & TellstickCoreLibrary.TELLSTICK_TURNOFF) > 0)
            devices.add(new OnOffDevice(deviceHandler, deviceId));

        // Up/Down
        if ((methods & TellstickCoreLibrary.TELLSTICK_UP) > 0 && (methods & TellstickCoreLibrary.TELLSTICK_DOWN) > 0 && (methods & TellstickCoreLibrary.TELLSTICK_STOP) > 0)
            devices.add(new UpDownDevice(deviceHandler, deviceId));

        // Scene
        if ((methods & TellstickCoreLibrary.TELLSTICK_EXECUTE) > 0)
            devices.add(new SceneDevice(deviceHandler, deviceId));
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void on() {
        logger.info("ON " + toString());

        for (Device device : getDevices()) {
            try {
                // On/off device
                if (device instanceof OnOffDevice) {
                    ((OnOffDevice) device).on();
                }
                // Dimmable device
                else if (device instanceof DimmableDevice) {
                    ((DimmableDevice) device).on();
                }
            } catch (DeviceException e) {
                logger.error("Failed to turn on device #" + device.getDeviceId());
            }
        }
    }

    public void off() {
        logger.info("ON " + toString());

        for (Device device : getDevices()) {
            try {
                // On/off device
                if (device instanceof OnOffDevice) {
                    ((OnOffDevice) device).off();
                }
                // Dimmable device
                else if (device instanceof DimmableDevice) {
                    ((DimmableDevice) device).off();
                }
            } catch (DeviceException e) {
                logger.error("Failed to turn on device #" + device.getDeviceId());
            }
        }
    }
}
