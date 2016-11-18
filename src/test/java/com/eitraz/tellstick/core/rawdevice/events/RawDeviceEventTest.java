package com.eitraz.tellstick.core.rawdevice.events;

import com.eitraz.tellstick.core.rawdevice.AbstractRawEventTest;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class RawDeviceEventTest extends AbstractRawEventTest {
    @Test
    public void properties() throws Exception {
        library.fireRawDeviceEvent("class:command;protocol:arctech;model:selflearning;house:A;unit:1;group:0;method:turnon;controllerId:5", 0);

        waitForNumberOfEvents(1);

        RawDeviceEvent event = event(0, RawDeviceEvent.class);
        assertThat(event.get_Class()).isEqualTo("command");
        assertThat(event.getProtocol()).isEqualTo("arctech");
        assertThat(event.getModel()).isEqualTo("selflearning");
        assertThat(event.getControllerId()).isEqualTo("5");
        assertThat(event.getParameters().get("house")).isEqualTo("A");
        assertThat(event.getParameters().get("unit")).isEqualTo("1");
        assertThat(event.getParameters().get("group")).isEqualTo("0");
        assertThat(event.getParameters().get("method")).isEqualTo("turnon");
        assertThat(event.getTime().isBefore(LocalDateTime.now()));

        assertThat(event.toString()).contains(
                "RawOnOffCommandEvent",
                "protocol=arctech",
                "unit=1",
                "controllerId=5",
                "method=turnon",
                "model=selflearning",
                "class=command",
                "house=A",
                "group=0");

        assertThat(event).isInstanceOf(RawCommandEvent.class);
        RawCommandEvent commandEvent = (RawCommandEvent) event;
        assertThat(commandEvent.getHouse()).isEqualTo("A");
        assertThat(commandEvent.getUnit()).isEqualTo("1");
        assertThat(commandEvent.getGroup()).isEqualTo("0");
        assertThat(commandEvent.getMethod()).isEqualTo("turnon");
    }
}