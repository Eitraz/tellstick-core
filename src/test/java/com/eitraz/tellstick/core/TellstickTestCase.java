package com.eitraz.tellstick.core;

import com.eitraz.tellstick.core.device.Device;
import com.eitraz.tellstick.core.device.DeviceHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

@Ignore
public class TellstickTestCase {
    @Test
    public void testGetDevices() {
        Tellstick tellstick = new Tellstick();
        try {
            tellstick.start();

            DeviceHandler deviceHandler = tellstick.getDeviceHandler();
            List<Device> devices = deviceHandler.getDevices();

            for (Device device : devices) {
                System.out.println(device);
            }
        } finally {
            tellstick.stop();
        }
    }

}
