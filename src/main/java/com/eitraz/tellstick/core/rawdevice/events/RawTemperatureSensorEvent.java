package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

public class RawTemperatureSensorEvent extends RawSensorEvent {

    public RawTemperatureSensorEvent(Map<String, String> parameters) {
        super(parameters);
    }

    public Double getTemperature() {
        return Double.valueOf(get(TEMP));
    }
}
