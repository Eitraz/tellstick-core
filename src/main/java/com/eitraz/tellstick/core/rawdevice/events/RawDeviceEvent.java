package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

public class RawDeviceEvent {
    public static final String _CLASS = "class";
    public static final String PROTOCOL = "protocol";
    public static final String MODEL = "model";

    public static final String CONTROLLER_ID = "controllerId";

    public static final String COMMAND = "command";
    public static final String SENSOR = "sensor";

    public static final String ID = "id";
    public static final String METHOD = "method";

    public static final String HOUSE = "house";
    public static final String UNIT = "unit";
    public static final String GROUP = "group";

    public static final String BELL = "bell";
    public static final String ON = "turnon";
    public static final String OFF = "turnoff";

    public static final String HUMIDITY = "humidity";
    public static final String TEMP = "temp";

    private Map<String, String> parameters;
    private final long time;

    public RawDeviceEvent(Map<String, String> parameters) {
        this.parameters = parameters;
        this.time = System.currentTimeMillis();
    }

    public String get(String key) {
        return parameters.get(key);
    }

    public String get_Class() {
        return get(_CLASS);
    }

    public String getControllerId() {
        return get(CONTROLLER_ID);
    }

    public String getModel() {
        return get(MODEL);
    }

    public String getProtocol() {
        return get(PROTOCOL);
    }

    public long getTime() {
        return time;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return hashCode() + " - " + getClass().getSimpleName() + " : " + parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !getClass().isAssignableFrom(o.getClass())) return false;

        RawDeviceEvent that = (RawDeviceEvent) o;

        if (get_Class() != null ? !get_Class().equals(that.get_Class()) : that.get_Class() != null) return false;
        if (getModel() != null ? !getModel().equals(that.getModel()) : that.getModel() != null) return false;
        if (getProtocol() != null ? !getProtocol().equals(that.getProtocol()) : that.getProtocol() != null)
            return false;
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
