package com.eitraz.tellstick.core.cluster;

import com.eitraz.tellstick.core.device.OnOffDevice;
import com.hazelcast.core.Hazelcast;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TellstickClusterTest {
    private TellstickCluster tellstick;

    @Before
    public void before() {
        tellstick = new TellstickCluster(Hazelcast.newHazelcastInstance());
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
        String name = tellstick.getTellstick().getDeviceHandler().getDevices().get(0).getName();

        for (int i = 0; i < 2; i++) {
            tellstick.getProxiedDeviceByName(name, OnOffDevice.class).on();
            Thread.sleep(1000);

            tellstick.getProxiedDeviceByName(name, OnOffDevice.class).off();
            Thread.sleep(1000);
        }
    }
}