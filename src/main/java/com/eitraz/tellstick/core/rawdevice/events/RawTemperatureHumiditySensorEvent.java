package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

public class RawTemperatureHumiditySensorEvent extends RawTemperatureSensorEvent {

    public RawTemperatureHumiditySensorEvent(Map<String, String> parameters) {
        super(parameters);
    }

    public Double getHumidity() {
        return Double.valueOf(get(HUMIDITY));
    }
}
