package com.eitraz.tellstick.core.rawdevice.events;

import java.time.LocalDateTime;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
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

    //    public static final String BELL = "bell";
    public static final String ON = "turnon";
    public static final String OFF = "turnoff";

    public static final String HUMIDITY = "humidity";
    public static final String TEMP = "temp";

    private Map<String, String> parameters;
    private final LocalDateTime time;

    public RawDeviceEvent(Map<String, String> parameters) {
        this.parameters = parameters;
        this.time = LocalDateTime.now();
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

    public LocalDateTime getTime() {
        return time;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", getClass().getSimpleName(), parameters);
    }
}
