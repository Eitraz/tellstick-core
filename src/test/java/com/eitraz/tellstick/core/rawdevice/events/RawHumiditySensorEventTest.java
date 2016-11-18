package com.eitraz.tellstick.core.rawdevice.events;

import com.eitraz.tellstick.core.rawdevice.AbstractRawEventTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RawHumiditySensorEventTest extends AbstractRawEventTest {
    @Test
    public void humiditySensorEvent() throws Exception {
        library.fireRawDeviceEvent("class:sensor;humidity:54.3", 0);

        waitForNumberOfEvents(1);
        RawHumiditySensorEvent event = event(0, RawHumiditySensorEvent.class);
        assertThat(event.getHumidity()).isEqualTo(54.3);
    }
}