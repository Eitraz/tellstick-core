package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

public class RawSensorEvent extends RawDeviceEvent {
    public RawSensorEvent(Map<String, String> parameters) {
        super(parameters);
    }

    public String getId() {
        return get(ID);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return true;
        if (!getClass().isAssignableFrom(o.getClass())) return false;

        RawSensorEvent that = (RawSensorEvent) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
