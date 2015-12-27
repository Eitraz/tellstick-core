package com.eitraz.tellstick.core.rawdevice.events.impl;

import com.eitraz.tellstick.core.rawdevice.events.RawHumiditySensorEvent;

import java.util.Map;

public class RawTemperatureHumiditySensorEventImpl extends RawTemperatureSensorEventImpl implements RawHumiditySensorEvent {

    public RawTemperatureHumiditySensorEventImpl(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public Double getHumidity() {
        return Double.valueOf(get(HUMIDITY));
    }
}
