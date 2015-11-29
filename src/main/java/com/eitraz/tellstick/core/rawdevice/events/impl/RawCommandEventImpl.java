package com.eitraz.tellstick.core.rawdevice.events.impl;

import com.eitraz.tellstick.core.rawdevice.events.RawCommandEvent;

import java.util.Map;

public class RawCommandEventImpl extends RawDeviceEventImpl implements RawCommandEvent {
    public RawCommandEventImpl(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public String getHouse() {
        return get(HOUSE);
    }

    @Override
    public String getUnit() {
        return get(UNIT);
    }

    @Override
    public String getGroup() {
        return get(GROUP);
    }

    @Override
    public String getMethod() {
        return get(METHOD);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return true;
        if (!getClass().isAssignableFrom(o.getClass())) return false;

        RawCommandEventImpl that = (RawCommandEventImpl) o;

        if (getHouse() != null ? !getHouse().equals(that.getHouse()) : that.getHouse() != null) return false;
        if (getUnit() != null ? !getUnit().equals(that.getUnit()) : that.getUnit() != null) return false;
        if (getGroup() != null ? !getGroup().equals(that.getGroup()) : that.getGroup() != null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getHouse() != null ? getHouse().hashCode() : 0);
        result = 31 * result + (getUnit() != null ? getUnit().hashCode() : 0);
        result = 31 * result + (getGroup() != null ? getGroup().hashCode() : 0);
        return result;
    }
}
