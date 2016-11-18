package com.eitraz.tellstick.core.sensor;

import com.eitraz.tellstick.core.sensor.value.SensorValue;

@SuppressWarnings("WeakerAccess")
public interface SensorEventListener {
    void sensorEvent(Sensor sensor, SensorValue value);
}
