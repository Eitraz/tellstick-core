package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import com.eitraz.tellstick.core.TellstickCoreLibrary;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpDownDeviceTest extends AbstractMockTellstickTest {
    @Test
    public void upDownStop() throws Exception {
        UpDownDevice device = createDefaultDevice(UpDownDevice.class, "test");

        device.up();
        assertThat(device.getStatus()).isEqualTo(TellstickCoreLibrary.TELLSTICK_UP);

        device.down();
        assertThat(device.getStatus()).isEqualTo(TellstickCoreLibrary.TELLSTICK_DOWN);

        device.stop();
        assertThat(device.getStatus()).isEqualTo(TellstickCoreLibrary.TELLSTICK_STOP);
    }
}