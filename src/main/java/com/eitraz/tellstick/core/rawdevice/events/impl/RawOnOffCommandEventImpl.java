package com.eitraz.tellstick.core.rawdevice.events.impl;

import com.eitraz.tellstick.core.rawdevice.events.RawOnOffCommandEvent;

import java.util.Map;

public class RawOnOffCommandEventImpl extends RawCommandEventImpl implements RawOnOffCommandEvent {
    public RawOnOffCommandEventImpl(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public boolean isTurnOff() {
        return OFF.equalsIgnoreCase(getMethod());
    }

    @Override
    public boolean isTurnOn() {
        return ON.equalsIgnoreCase(getMethod());
    }
}
