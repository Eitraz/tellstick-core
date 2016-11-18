package com.eitraz.tellstick.core.rawdevice.events;

import com.eitraz.tellstick.core.rawdevice.AbstractRawEventTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RawTemperatureHumiditySensorEventTest extends AbstractRawEventTest {
    @Test
    public void temperatureHumiditySensorEvent() throws Exception {
        library.fireRawDeviceEvent("class:sensor;id:112;temp:19.7;humidity:54.3", 0);

        waitForNumberOfEvents(1);
        RawTemperatureHumiditySensorEvent event = event(0, RawTemperatureHumiditySensorEvent.class);

        assertThat(event.getId()).isEqualTo("112");
        assertThat(event.getTemperature()).isEqualTo(19.7);
        assertThat(event.getHumidity()).isEqualTo(54.3);
    }
}