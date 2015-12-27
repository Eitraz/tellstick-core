package com.eitraz.tellstick.core.rawdevice;

import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;

public interface RawDeviceEventListener {
    void rawDeviceEvent(RawDeviceEvent event);
}
