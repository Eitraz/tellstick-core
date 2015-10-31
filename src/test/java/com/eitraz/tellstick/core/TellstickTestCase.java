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
        TellstickImpl tellstick = new TellstickImpl();
        try {
            tellstick.start();

            DeviceHandler deviceHandler = tellstick.getDeviceHandler();
            List<Device> devices = deviceHandler.getDevices();

            devices.forEach(System.out::println);
        } finally {
            tellstick.stop();
        }
    }

}
