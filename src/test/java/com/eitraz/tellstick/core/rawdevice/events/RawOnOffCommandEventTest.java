package com.eitraz.tellstick.core.rawdevice.events;

import com.eitraz.tellstick.core.rawdevice.AbstractRawEventTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RawOnOffCommandEventTest extends AbstractRawEventTest {
    @Test
    public void rawOnOffCommandEvent() throws Exception {
        library.fireRawDeviceEvent("class:command;protocol:arctech;model:selflearning;house:A;unit:1;group:0;method:turnon", 0);
        library.fireRawDeviceEvent("class:command;protocol:arctech;model:selflearning;house:A;unit:1;group:0;method:turnoff", 0);

        waitForNumberOfEvents(2);

        RawOnOffCommandEvent event1 = event(0, RawOnOffCommandEvent.class);
        assertThat(event1.isTurnOn()).isTrue();
        assertThat(event1.isTurnOff()).isFalse();

        RawOnOffCommandEvent event2 = event(1, RawOnOffCommandEvent.class);
        assertThat(event2.isTurnOff()).isTrue();
        assertThat(event2.isTurnOn()).isFalse();
    }
}