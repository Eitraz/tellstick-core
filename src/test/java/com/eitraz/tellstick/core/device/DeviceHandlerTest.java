package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.Tellstick;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class DeviceHandlerTest {
    private Tellstick tellstick = new Tellstick();

    @Before
    public void before() {
        tellstick.start();
    }

    @After
    public void after() {
        tellstick.stop();
    }

    @Test
    public void testCreateDevice() throws Exception {
        Device device = null;
        try {
            int numDevices = tellstick.getDeviceHandler().getDevices().size();

            Map<String, String> parameters = new HashMap<>();
            parameters.put("house", "A");
            parameters.put("unit", "2");
            device = tellstick.getDeviceHandler().createDevice("Test Device", "codeswitch", "arctech", parameters);

            System.out.println("New device: " + device);

            assertEquals(numDevices + 1, tellstick.getDeviceHandler().getDevices().size());

            assertTrue(tellstick.getDeviceHandler().removeDevice(device));
            assertEquals(numDevices, tellstick.getDeviceHandler().getDevices().size());
            device = null;
        } finally {
            if (device != null) {
                tellstick.getDeviceHandler().removeDevice(device);
            }
        }
    }
}