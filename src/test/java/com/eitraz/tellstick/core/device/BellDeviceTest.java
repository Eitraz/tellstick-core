package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import org.junit.Test;

public class BellDeviceTest extends AbstractMockTellstickTest {
    @Test
    public void bell() throws Exception {
        BellDevice device = createDefaultDevice(BellDevice.class, "test");
        device.bell();
    }

}