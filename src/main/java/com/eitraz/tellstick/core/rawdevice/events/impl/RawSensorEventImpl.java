package com.eitraz.tellstick.core.rawdevice.events.impl;

import com.eitraz.tellstick.core.rawdevice.events.RawSensorEvent;

import java.util.Map;

public class RawSensorEventImpl extends RawDeviceEventImpl implements RawSensorEvent {
    public RawSensorEventImpl(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public String getId() {
        return get(ID);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return true;
        if (!getClass().isAssignableFrom(o.getClass())) return false;

        RawSensorEventImpl that = (RawSensorEventImpl) o;

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
