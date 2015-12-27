package com.eitraz.tellstick.core.rawdevice.events;

import java.io.Serializable;

public interface RawDeviceEvent extends Serializable {
    String _CLASS = "class";
    String PROTOCOL = "protocol";
    String MODEL = "model";

    String CONTROLLER_ID = "controllerId";

    String COMMAND = "command";
    String SENSOR = "sensor";

    String ID = "id";
    String METHOD = "method";

    String HOUSE = "house";
    String UNIT = "unit";
    String GROUP = "group";

    String BELL = "bell";
    String ON = "turnon";
    String OFF = "turnoff";

    String HUMIDITY = "humidity";
    String TEMP = "temp";

    String get(String key);

    String get_Class();

    String getControllerId();

    String getModel();

    String getProtocol();

    long getTime();
}
