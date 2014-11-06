package com.eitraz.tellstick.core;

import com.eitraz.tellstick.core.device.OnOffDevice;
import com.eitraz.tellstick.core.proxy.DeviceProxy;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

public class DeviceProxyTest {

    @Test
    public void testProxy() throws Exception {
        final TestDevice device = new TestDevice(1, "Test");

        DeviceProxy.getInstance().setTries(3);

        DeviceProxy.proxy(device, OnOffDevice.class).on();

        await().until(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return device.getCounter();
            }
        }, equalTo(3));
    }

}