package com.eitraz.tellstick.core.rawdevice.events;

public interface RawCommandEvent extends RawDeviceEvent {
    String getHouse();

    String getUnit();

    String getGroup();

    String getMethod();
}
