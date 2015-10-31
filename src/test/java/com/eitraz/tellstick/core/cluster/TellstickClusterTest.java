package com.eitraz.tellstick.core.cluster;

import com.eitraz.tellstick.core.device.OnOffDevice;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TellstickClusterTest {
    private TellstickCluster tellstick;

    @Before
    public void before() {
        tellstick = new TellstickCluster();
        tellstick.doStart();
    }

    @After
    public void after() {
        if (tellstick != null) {
            tellstick.doStop();
            tellstick = null;
        }
    }

    @Test
    public void testToggleDevice() throws Exception {
        for (int i = 0; i < 2; i++) {
            tellstick.getProxiedDeviceByName("Test", OnOffDevice.class).on();
            Thread.sleep(1000);

            tellstick.getProxiedDeviceByName("Test", OnOffDevice.class).off();
            Thread.sleep(1000);
        }
    }
}