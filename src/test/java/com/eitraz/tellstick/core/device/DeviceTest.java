package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeviceTest extends AbstractMockTellstickTest {
    @Test
    public void compare() throws Exception {
        OnOffDevice device_a = createDevice(OnOffDevice.class,
                "test1",
                MODEL_CODESWITCH,
                PROTOCOL_ARCTECH,
                ImmutableMap.of(
                        PARAMETER_HOUSE, "A",
                        PARAMETER_UNIT, "1"));

        OnOffDevice device_b = createDevice(OnOffDevice.class,
                "test2",
                MODEL_CODESWITCH,
                PROTOCOL_ARCTECH,
                ImmutableMap.of(
                        PARAMETER_HOUSE, "A",
                        PARAMETER_UNIT, "1"));

        assertThat(device_a.compareTo(device_b)).isLessThan(0);
        assertThat(device_a.equals(device_b)).isFalse();
        assertThat(device_a.hashCode()).isEqualTo(1);
        assertThat(device_b.hashCode()).isEqualTo(2);

        assertThat(device_a.getDeviceHandler()).isNotNull();

        assertThat(device_a.toString()).contains(
                "OnOffDevice",
                "deviceId=1",
                "name=OnOffDevice_test1",
                "model=codeswitch",
                "protocol=arctech",
                "deviceType=1");
    }
}