package com.eitraz.tellstick.core.sensor;

import com.eitraz.tellstick.core.TellstickException;

public class SensorException extends TellstickException {
    public SensorException(Sensor sensor, int errorNo) {
        super(String.format("Sensor: %d", sensor.getId()), sensor.getSensorHandler().getLibrary(), errorNo);
    }
}
