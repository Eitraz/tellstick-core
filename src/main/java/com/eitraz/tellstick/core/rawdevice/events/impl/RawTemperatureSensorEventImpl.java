package com.eitraz.tellstick.core.rawdevice.events.impl;

import com.eitraz.tellstick.core.rawdevice.events.RawTemperatureSensorEvent;

import java.util.Map;

public class RawTemperatureSensorEventImpl extends RawSensorEventImpl implements RawTemperatureSensorEvent {

    public RawTemperatureSensorEventImpl(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public Double getTemperature() {
        return Double.valueOf(get(TEMP));
    }

}
