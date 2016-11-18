package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.AbstractMockTellstickTest;
import com.eitraz.tellstick.core.TellstickCoreLibrary;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SceneDeviceTest extends AbstractMockTellstickTest {
    @Test
    public void execute() throws Exception {
        SceneDevice device = createDefaultDevice(SceneDevice.class, "test");
        device.execute();
        assertThat(device.getStatus()).isEqualTo(TellstickCoreLibrary.TELLSTICK_EXECUTE);
    }
}