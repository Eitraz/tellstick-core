package com.eitraz.tellstick.core.rawdevice.events;

public interface RawHumiditySensorEvent extends RawSensorEvent {
    Double getHumidity();
}
