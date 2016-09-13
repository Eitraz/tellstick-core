package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

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

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return true;
        if (!getClass().isAssignableFrom(o.getClass())) return false;

        RawCommandEvent that = (RawCommandEvent) o;

        if (getHouse() != null ? !getHouse().equals(that.getHouse()) : that.getHouse() != null) return false;
        if (getUnit() != null ? !getUnit().equals(that.getUnit()) : that.getUnit() != null) return false;
        if (getGroup() != null ? !getGroup().equals(that.getGroup()) : that.getGroup() != null) return false;
        if (getMethod() != null ? !getMethod().equals(that.getMethod()) : that.getMethod() != null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getHouse() != null ? getHouse().hashCode() : 0);
        result = 31 * result + (getUnit() != null ? getUnit().hashCode() : 0);
        result = 31 * result + (getGroup() != null ? getGroup().hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        return result;
    }
}
