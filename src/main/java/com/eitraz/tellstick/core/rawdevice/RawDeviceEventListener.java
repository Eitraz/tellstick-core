package com.eitraz.tellstick.core.rawdevice;

import java.util.Map;

public interface RawDeviceEventListener {
    void rawDeviceEvent(Map<String, String> parameters);
}
