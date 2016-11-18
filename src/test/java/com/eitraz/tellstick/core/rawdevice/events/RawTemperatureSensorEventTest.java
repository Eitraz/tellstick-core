package com.eitraz.tellstick.core.rawdevice.events;

import com.eitraz.tellstick.core.rawdevice.AbstractRawEventTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RawTemperatureSensorEventTest extends AbstractRawEventTest {
    @Test
    public void temperatureSensorEvent() throws Exception {
        library.fireRawDeviceEvent("class:sensor;temp:19.7", 0);

        waitForNumberOfEvents(1);
        RawTemperatureSensorEvent event = event(0, RawTemperatureSensorEvent.class);
        assertThat(event.getTemperature()).isEqualTo(19.7);
    }
}