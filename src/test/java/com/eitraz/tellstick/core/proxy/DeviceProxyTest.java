package com.eitraz.tellstick.core.proxy;

import com.eitraz.tellstick.core.device.*;
import com.jayway.awaitility.Awaitility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class DeviceProxyTest {
    public static final int TRIES = 3;

    public DeviceProxyTest() {
        Awaitility.setDefaultTimeout(5, TimeUnit.SECONDS);
    }

    @Before
    public void before() {
        // Setup
        DeviceProxy.getInstance().setCallDelay(250);
        DeviceProxy.getInstance().setTries(TRIES);
        DeviceProxy.getInstance().start();
    }

    @After
    public void after() {
        DeviceProxy.getInstance().stop();
    }

    @Test
    public void testBellDevice() throws Exception {
        TestBellDevice device = new TestBellDevice(1, "Test");
        DeviceProxy.proxy(device, BellDevice.class).bell();
        assertExpectedCount(device);
    }

    @Test
    public void testDimmableDevice() throws Exception {
        TestDimmableDevice device = new TestDimmableDevice(1, "Test");
        DeviceProxy.proxy(device, DimmableDevice.class).on();
        assertExpectedCount(device);
    }

    @Test
    public void testGroupDevice() throws Exception {
        TestGroupDevice device = new TestGroupDevice(1, "Test");
        DeviceProxy.proxy(device, GroupDevice.class).on();
        assertExpectedCount(device);
    }

    @Test
    public void testOnOffDevice() throws Exception {
        TestOnOffDevice device = new TestOnOffDevice(1, "Test");
        DeviceProxy.proxy(device, OnOffDevice.class).on();
        assertExpectedCount(device);
    }

    @Test
    public void testSceneDevice() throws Exception {
        TestSceneDevice device = new TestSceneDevice(1, "Test");
        DeviceProxy.proxy(device, SceneDevice.class).execute();
        assertExpectedCount(device);
    }

    @Test
    public void testUpDownDevice() throws Exception {
        TestUpDownDevice device = new TestUpDownDevice(1, "Test");
        DeviceProxy.proxy(device, UpDownDevice.class).up();
        assertExpectedCount(device);
    }

    @Test
    public void testMultiple() throws Exception {
        int deviceId = 0;

        TestBellDevice bellDevice = new TestBellDevice(++deviceId, "Test");
        TestDimmableDevice dimmableDevice = new TestDimmableDevice(++deviceId, "Test");
        TestGroupDevice groupDevice = new TestGroupDevice(++deviceId, "Test");
        TestOnOffDevice onOffDevice = new TestOnOffDevice(++deviceId, "Test");
        TestSceneDevice sceneDevice = new TestSceneDevice(++deviceId, "Test");
        TestUpDownDevice upDownDevice = new TestUpDownDevice(++deviceId, "Test");

        DeviceProxy.proxy(bellDevice, BellDevice.class).bell();
        DeviceProxy.proxy(dimmableDevice, DimmableDevice.class).on();
        DeviceProxy.proxy(groupDevice, GroupDevice.class).on();
        DeviceProxy.proxy(onOffDevice, OnOffDevice.class).on();
        DeviceProxy.proxy(sceneDevice, SceneDevice.class).execute();
        DeviceProxy.proxy(upDownDevice, UpDownDevice.class).up();

        assertExpectedCount(bellDevice);
        assertExpectedCount(dimmableDevice);
        assertExpectedCount(groupDevice);
        assertExpectedCount(onOffDevice);
        assertExpectedCount(sceneDevice);
        assertExpectedCount(upDownDevice);
    }

    @Test
    public void testOverride() throws Exception {
        DeviceProxy.getInstance().setCallDelay(500);

        final TestOnOffDevice onOffDevice = new TestOnOffDevice(0, "Test");

        DeviceProxy.proxy(onOffDevice, OnOffDevice.class).on();

        // Wait for one try
        await().until(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return onOffDevice.getCounter();
            }
        }, equalTo(2));

        DeviceProxy.proxy(onOffDevice, OnOffDevice.class).off();

        Thread.sleep(600 * 3);
        assertEquals(-1, onOffDevice.getCounter());
    }

    /**
     * Assert expected count
     *
     * @param device device
     * @throws Exception
     */
    private void assertExpectedCount(final AbstractTestDevice device) throws Exception {
        // Wait for all tries
        await().until(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return device.getCounter();
            }
        }, equalTo(TRIES));

        // Make sure there is no other tries
        Thread.sleep(500);
        assertEquals(TRIES, device.getCounter());
    }

}