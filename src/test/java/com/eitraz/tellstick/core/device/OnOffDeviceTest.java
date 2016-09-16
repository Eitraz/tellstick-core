package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OnOffDeviceTest extends AbstractMockTellstickTest {
    @Test
    public void onOff() throws Exception {
        OnOffDevice device = createDefaultDevice(OnOffDevice.class, "test");

        device.on();

        assertThat(device.isOn()).isTrue();
        assertThat(device.isOff()).isFalse();

        device.off();

        assertThat(device.isOn()).isFalse();
        assertThat(device.isOff()).isTrue();
    }
}