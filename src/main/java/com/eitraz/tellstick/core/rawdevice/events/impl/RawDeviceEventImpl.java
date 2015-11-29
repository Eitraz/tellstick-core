package com.eitraz.tellstick.core.rawdevice.events.impl;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;

import java.util.Map;

public class RawDeviceEventImpl implements RawDeviceEvent {
    private Map<String, String> parameters;

    public RawDeviceEventImpl(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String get(String key) {
        return parameters.get(key);
    }

    @Override
    public String get_Class() {
        return get(_CLASS);
    }

    @Override
    public String getControllerId() {
        return get(CONTROLLER_ID);
    }

    @Override
    public String getModel() {
        return get(MODEL);
    }

    @Override
    public String getProtocol() {
        return get(PROTOCOL);
    }

    @Override
    public String toString() {
        return hashCode() + " - " + getClass().getSimpleName() + " : " + parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().isAssignableFrom(o.getClass())) return false;

        RawDeviceEventImpl that = (RawDeviceEventImpl) o;

        if (get_Class() != null ? !get_Class().equals(that.get_Class()) : that.get_Class() != null) return false;
        if (getModel() != null ? !getModel().equals(that.getModel()) : that.getModel() != null) return false;
        if (getProtocol() != null ? !getProtocol().equals(that.getProtocol()) : that.getProtocol() != null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = get_Class() != null ? get_Class().hashCode() : 0;
        result = 31 * result + (getModel() != null ? getModel().hashCode() : 0);
        result = 31 * result + (getProtocol() != null ? getProtocol().hashCode() : 0);
        return result;
    }
}
