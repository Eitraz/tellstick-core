package com.eitraz.tellstick.core.sensor.value;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

import java.time.LocalDateTime;

public class HumiditySensorValue extends SensorValue {
    public HumiditySensorValue(String value, LocalDateTime time) {
        super(TellstickCoreLibrary.TELLSTICK_HUMIDITY, value, time);
    }

    public double getHumidity() {
        return Double.parseDouble(getValue());
    }
}
