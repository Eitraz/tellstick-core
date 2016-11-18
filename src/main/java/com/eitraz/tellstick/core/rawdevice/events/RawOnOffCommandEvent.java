package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

public class RawOnOffCommandEvent extends RawCommandEvent {
    public RawOnOffCommandEvent(Map<String, String> parameters) {
        super(parameters);
    }

    public boolean isTurnOff() {
        return OFF.equalsIgnoreCase(getMethod());
    }

    public boolean isTurnOn() {
        return ON.equalsIgnoreCase(getMethod());
    }
}
