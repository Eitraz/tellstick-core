package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class RawCommandEvent extends RawDeviceEvent {
    public RawCommandEvent(Map<String, String> parameters) {
        super(parameters);
    }

    public String getHouse() {
        return get(HOUSE);
    }

    public String getUnit() {
        return get(UNIT);
    }

    public String getGroup() {
        return get(GROUP);
    }

    public String getMethod() {
        return get(METHOD);
    }
}
