package com.eitraz.tellstick.core.rawdevice.events;

import java.util.Map;

public class RawHumiditySensorEvent extends RawSensorEvent {
    public RawHumiditySensorEvent(Map<String, String> parameters) {
        super(parameters);
    }

    public Double getHumidity() {
        return Double.valueOf(get(HUMIDITY));
    }
}
