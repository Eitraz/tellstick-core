package com.eitraz.tellstick.core.rawdevice.events;

public interface RawTemperatureSensorEvent extends RawSensorEvent {
    Double getTemperature();
}
