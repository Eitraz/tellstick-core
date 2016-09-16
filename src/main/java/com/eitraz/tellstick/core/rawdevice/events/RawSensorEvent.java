package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

public class RawSensorEvent extends RawDeviceEvent {
    public RawSensorEvent(Map<String, String> parameters) {
        super(parameters);
    }

    public String getId() {
        return get(ID);
    }
}
