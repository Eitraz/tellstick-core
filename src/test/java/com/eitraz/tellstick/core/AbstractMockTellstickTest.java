package com.eitraz.tellstick.core;

import com.eitraz.tellstick.core.device.Device;
import com.eitraz.tellstick.core.device.DeviceNotSupportedException;
import com.google.common.collect.ImmutableMap;
import org.junit.Rule;

import java.util.Map;

import static com.eitraz.tellstick.core.TellstickCoreLibrary.TELLSTICK_TYPE_DEVICE;
import static com.eitraz.tellstick.core.TellstickCoreLibrary.TELLSTICK_TYPE_SCENE;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractMockTellstickTest {
    public static final String MODEL_CODESWITCH = "codeswitch";
    public static final String PROTOCOL_ARCTECH = "arctech";

    public static final String PARAMETER_HOUSE = "house";
    public static final String PARAMETER_UNIT = "unit";

    @Rule
    public final TellstickRule tellstickRule = new TellstickRule();

    protected final TellstickCoreMockLibrary library = tellstickRule.getLibrary();
    protected final Tellstick tellstick = tellstickRule.getTellstick();

    protected <T extends Device> T createDevice(Class<T> type, String name, String model, String protocol, Map<String, String> parameters) {
        try {
            String fullName = String.format("%s_%s", type.getSimpleName(), name);
            T device = tellstick.getDeviceHandler().createDevice(
                    fullName,
                    model,
                    protocol,
                    parameters);

            // Validate device
            assertThat(device.getDeviceId()).isNotNull();
            assertThat(device.getName()).isEqualTo(fullName);
            assertThat(device.getModel()).isEqualTo(model);
            assertThat(device.getProtocol()).isEqualTo(protocol);
            parameters.forEach((key, value) -> assertThat(device.getParameter(key)).isEqualTo(value));
            assertThat(device.getDeviceType()).isBetween(TELLSTICK_TYPE_DEVICE, TELLSTICK_TYPE_SCENE);

            return device;
        } catch (TellstickException | DeviceNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends Device> T createDefaultDevice(Class<T> type, String name) {
        return createDevice(type,
                name,
                MODEL_CODESWITCH,
                PROTOCOL_ARCTECH,
                ImmutableMap.of(
                        PARAMETER_HOUSE, "A",
                        PARAMETER_UNIT, "1"));
    }
}
