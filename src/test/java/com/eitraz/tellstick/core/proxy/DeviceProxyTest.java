package com.eitraz.tellstick.core.proxy;

import com.eitraz.tellstick.core.TestDevice;
import com.eitraz.tellstick.core.device.OnOffDevice;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class DeviceProxyTest {

    @Test
    public void testProxy() throws Exception {
        final TestDevice device = new TestDevice(1, "Test");

        // Setup
        DeviceProxy.getInstance().setCallDelay(500);
        DeviceProxy.getInstance().setTries(3);

        // Run
        DeviceProxy.proxy(device, OnOffDevice.class).on();

        // Wait for all tries
        await().until(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return device.getCounter();
            }
        }, equalTo(3));

        // Make sure there is no other tries
        Thread.sleep(750);
        assertEquals(3, device.getCounter());
    }

}