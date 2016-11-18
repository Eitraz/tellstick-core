package com.eitraz.tellstick.core.sensor.value;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

import java.time.LocalDateTime;

public class TemperatureSensorValue extends SensorValue {
    public TemperatureSensorValue(String value, LocalDateTime time) {
        super(TellstickCoreLibrary.TELLSTICK_TEMPERATURE, value, time);
    }

    public double getTemperature() {
        return Double.parseDouble(getValue());
    }
}
