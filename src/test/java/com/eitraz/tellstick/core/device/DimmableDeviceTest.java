package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import com.eitraz.tellstick.core.TellstickCoreLibrary;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DimmableDeviceTest extends AbstractMockTellstickTest {
    @Test
    public void onOff() throws Exception {
        DimmableDevice device = createDefaultDevice(DimmableDevice.class, "test");

        device.on();

        assertThat(device.isOn()).isTrue();
        assertThat(device.isOff()).isFalse();

        device.off();

        assertThat(device.isOn()).isFalse();
        assertThat(device.isOff()).isTrue();
    }

    @Test
    public void dim() throws Exception {
        DimmableDevice device = createDefaultDevice(DimmableDevice.class, "test");
        device.dim(150);
        assertThat(device.getStatus()).isEqualTo(TellstickCoreLibrary.TELLSTICK_DIM);
    }

}