package com.eitraz.tellstick.core.sensor.value;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class SensorValue {
    private final int dataType;
    private final String value;
    private final LocalDateTime time;

    public SensorValue(int dataType, String value, LocalDateTime time) {
        this.dataType = dataType;
        this.value = value;
        this.time = time;
    }

    public int getDataType() {
        return dataType;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getTimestamp() {
        return time;
    }

    @SuppressWarnings("unchecked")
    public static <T extends SensorValue> T parseSensorValue(int dataType, int timestamp, String value) {
        LocalDateTime time = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);

        // Temperature
        if (dataType == TellstickCoreLibrary.TELLSTICK_TEMPERATURE) {
            return (T) new TemperatureSensorValue(value, time);
        }
        // Humidity
        else if (dataType == TellstickCoreLibrary.TELLSTICK_HUMIDITY) {
            return (T) new HumiditySensorValue(value, time);
        }
        // Other
        else {
            return (T) new SensorValue(dataType, value, time);
        }
    }

    @Override
    public String toString() {
        return "SensorValue{" +
                "dataType=" + dataType +
                ", value='" + value + '\'' +
                ", time=" + time +
                '}';
    }
}
