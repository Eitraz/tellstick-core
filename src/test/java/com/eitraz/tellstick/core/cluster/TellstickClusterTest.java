package com.eitraz.tellstick.core.cluster;

import com.eitraz.tellstick.core.device.OnOffDevice;
import org.junit.After;
import org.junit.Before;
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
        tellstick.getProxiedDeviceByName("Example device", OnOffDevice.class).on();
    }
}