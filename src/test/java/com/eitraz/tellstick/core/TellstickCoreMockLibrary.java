package com.eitraz.tellstick.core;

import com.eitraz.tellstick.core.device.*;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TellstickCoreMockLibrary implements TellstickCoreLibrary {
    private int deviceIdCounter = 1;

    private Map<Integer, Integer> lastCommands = new HashMap<>();
    private List<DeviceMock> devices = new ArrayList<>();
    private List<SensorMock> sensors = new ArrayList<>();
    private int tdSensorCounter = 0;

    private int callbackIdCounter = 1;

    private Map<Integer, TDDeviceEvent> deviceEventListeners = new HashMap<>();
    private Map<Integer, TDDeviceChangeEvent> deviceChangeEventListeners = new HashMap<>();
    private Map<Integer, TDRawDeviceEvent> rawDeviceEventListeners = new HashMap<>();
    private Map<Integer, TDSensorEvent> sensorEventListeners = new HashMap<>();
    private Map<Integer, TDControllerEvent> controllerEventListeners = new HashMap<>();

    @Override
    public void tdInit() {
    }

    @Override
    public int tdRegisterDeviceEvent(TDDeviceEvent eventFunction, Pointer context) {
        int callbackId = callbackIdCounter++;
        deviceEventListeners.put(callbackId, eventFunction);
        return callbackId;
    }

    @Override
    public int tdRegisterDeviceChangeEvent(TDDeviceChangeEvent eventFunction, Pointer context) {
        int callbackId = callbackIdCounter++;
        deviceChangeEventListeners.put(callbackId, eventFunction);
        return callbackId;
    }

    @Override
    public int tdRegisterRawDeviceEvent(TDRawDeviceEvent eventFunction, Pointer context) {
        int callbackId = callbackIdCounter++;
        rawDeviceEventListeners.put(callbackId, eventFunction);
        return callbackId;
    }

    @Override
    public int tdRegisterSensorEvent(TDSensorEvent eventFunction, Pointer context) {
        int callbackId = callbackIdCounter++;
        sensorEventListeners.put(callbackId, eventFunction);
        return callbackId;
    }

    @Override
    public int tdRegisterControllerEvent(TDControllerEvent eventFunction, Pointer context) {
        int callbackId = callbackIdCounter++;
        controllerEventListeners.put(callbackId, eventFunction);
        return callbackId;
    }

    @Override
    public int tdUnregisterCallback(int callbackId) {
        deviceEventListeners.remove(callbackId);
        deviceChangeEventListeners.remove(callbackId);
        rawDeviceEventListeners.remove(callbackId);
        sensorEventListeners.remove(callbackId);
        controllerEventListeners.remove(callbackId);
        return TELLSTICK_SUCCESS;
    }

    @Override
    public void tdClose() {
    }

    @Override
    public void tdReleaseString(Pointer thestring) {

    }

    private void storeLastCommand(int deviceId, int command) {
        lastCommands.put(deviceId, command);
    }

    @Override
    public int tdTurnOn(int intDeviceId) {
        getMockDeviceById(intDeviceId).on = true;
        storeLastCommand(intDeviceId, TELLSTICK_TURNON);

        fireDeviceEvent(intDeviceId, TELLSTICK_TURNON, "");

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdTurnOff(int intDeviceId) {
        getMockDeviceById(intDeviceId).on = false;
        storeLastCommand(intDeviceId, TELLSTICK_TURNOFF);

        fireDeviceEvent(intDeviceId, TELLSTICK_TURNOFF, "");

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdBell(int intDeviceId) {
        storeLastCommand(intDeviceId, TELLSTICK_BELL);

        fireDeviceEvent(intDeviceId, TELLSTICK_BELL, "");

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdDim(int intDeviceId, int level) {
        storeLastCommand(intDeviceId, TELLSTICK_DIM);

        getMockDeviceById(intDeviceId).dim = level;

        fireDeviceEvent(intDeviceId, TELLSTICK_DIM, String.valueOf(level));

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdExecute(int intDeviceId) {
        storeLastCommand(intDeviceId, TELLSTICK_EXECUTE);

        fireDeviceEvent(intDeviceId, TELLSTICK_EXECUTE, "");

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdUp(int intDeviceId) {
        storeLastCommand(intDeviceId, TELLSTICK_UP);

        getMockDeviceById(intDeviceId).up = true;

        fireDeviceEvent(intDeviceId, TELLSTICK_UP, "");

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdDown(int intDeviceId) {
        storeLastCommand(intDeviceId, TELLSTICK_DOWN);

        getMockDeviceById(intDeviceId).up = false;

        fireDeviceEvent(intDeviceId, TELLSTICK_DOWN, "");

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdStop(int intDeviceId) {
        storeLastCommand(intDeviceId, TELLSTICK_STOP);

        fireDeviceEvent(intDeviceId, TELLSTICK_STOP, "");

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdLearn(int intDeviceId) {
        storeLastCommand(intDeviceId, TELLSTICK_LEARN);

        fireDeviceEvent(intDeviceId, TELLSTICK_LEARN, "");

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdMethods(int intDeviceId, int methodsSupported) {
        return getMockDeviceById(intDeviceId).methods;
    }

    @Override
    public int tdLastSentCommand(int intDeviceId, int methodsSupported) {
        return lastCommands.get(intDeviceId);
    }

    @Override
    public int tdGetNumberOfDevices() {
        return devices.size();
    }

    @Override
    public int tdGetDeviceId(int intDeviceIndex) {
        return devices.get(intDeviceIndex).id;
    }

    public DeviceMock getMockDeviceById(int deviceId) {
        return devices.stream()
                .filter(d -> d.id == deviceId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public int tdGetDeviceType(int intDeviceId) {
        return getMockDeviceById(intDeviceId).type;
    }

    private Pointer mockPointer(String string) {
        Pointer pointer = mock(Pointer.class);
        when(pointer.getString(0)).thenReturn(string);
        return pointer;
    }

    @Override
    public Pointer tdGetErrorString(int intErrorNo) {
        return mockPointer("Error: " + intErrorNo);
    }

    @Override
    public Pointer tdGetName(int intDeviceId) {
        return mockPointer(getMockDeviceById(intDeviceId).name);
    }

    @Override
    public boolean tdSetName(int intDeviceId, String chNewName) {
        DeviceMock device = getMockDeviceById(intDeviceId);
        device.name = chNewName;

        // Group
        if (chNewName.contains(GroupDevice.class.getSimpleName())) {
            device.type |= TELLSTICK_TYPE_GROUP;
        }
        // Scene
        else if (chNewName.contains(SceneDevice.class.getSimpleName())) {
            device.type |= TELLSTICK_TYPE_SCENE;
            device.methods |= TELLSTICK_EXECUTE;
        }
        // Bell
        else if (chNewName.contains(BellDevice.class.getSimpleName())) {
            device.methods |= TELLSTICK_BELL;
            device.type |= TELLSTICK_TYPE_DEVICE;
        }
        // Dimmable
        else if (chNewName.contains(DimmableDevice.class.getSimpleName())) {
            device.methods |= TELLSTICK_DIM;
            device.type |= TELLSTICK_TYPE_DEVICE;
        }
        // Up / Down
        else if (chNewName.contains(UpDownDevice.class.getSimpleName())) {
            device.methods |= TELLSTICK_UP | TELLSTICK_DOWN | TELLSTICK_STOP;
            device.type |= TELLSTICK_TYPE_DEVICE;
        }
        // On / Off
        else if (chNewName.contains(OnOffDevice.class.getSimpleName())) {
            device.methods |= TELLSTICK_TURNON | TELLSTICK_TURNOFF;
            device.type |= TELLSTICK_TYPE_DEVICE;
        }

        fireDeviceChangeEvent(device.id, TELLSTICK_DEVICE_CHANGED, TELLSTICK_CHANGE_NAME);

        return true;
    }

    @Override
    public Pointer tdGetProtocol(int intDeviceId) {
        return mockPointer(getMockDeviceById(intDeviceId).protocol);
    }

    @Override
    public boolean tdSetProtocol(int intDeviceId, String strProtocol) {
        getMockDeviceById(intDeviceId).protocol = strProtocol;

        fireDeviceChangeEvent(intDeviceId, TELLSTICK_DEVICE_CHANGED, TELLSTICK_CHANGE_PROTOCOL);

        return true;
    }

    @Override
    public Pointer tdGetModel(int intDeviceId) {
        return mockPointer(getMockDeviceById(intDeviceId).model);
    }

    @Override
    public boolean tdSetModel(int intDeviceId, String intModel) {
        getMockDeviceById(intDeviceId).model = intModel;

        fireDeviceChangeEvent(intDeviceId, TELLSTICK_DEVICE_CHANGED, TELLSTICK_CHANGE_MODEL);

        return true;
    }

    @Override
    public Pointer tdGetDeviceParameter(int intDeviceId, String strName, String defaultValue) {
        return mockPointer(getMockDeviceById(intDeviceId).parameter.getOrDefault(strName, defaultValue));
    }

    @Override
    public boolean tdSetDeviceParameter(int intDeviceId, String strName, String strValue) {
        getMockDeviceById(intDeviceId).parameter.put(strName, strValue);

        fireDeviceChangeEvent(intDeviceId, TELLSTICK_DEVICE_CHANGED, 0);

        return true;
    }

    @Override
    public int tdAddDevice() {
        DeviceMock device = new DeviceMock(deviceIdCounter++);

        // Default to on/off device
        device.methods |= TELLSTICK_TURNON | TELLSTICK_TURNOFF;
        device.type |= TELLSTICK_TYPE_DEVICE;

        devices.add(device);

        fireDeviceChangeEvent(device.id, TELLSTICK_DEVICE_ADDED, TELLSTICK_CHANGE_AVAILABLE);

        return device.id;
    }

    @Override
    public boolean tdRemoveDevice(int intDeviceId) {
        boolean removed = devices.remove(getMockDeviceById(intDeviceId));

        if (removed) {
            fireDeviceChangeEvent(intDeviceId, TELLSTICK_DEVICE_REMOVED, 0);
        }

        return removed;
    }

    @Override
    public int tdSendRawCommand(String command, int reserved) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void tdConnectTellStickController(int vid, int pid, String serial) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void tdDisconnectTellStickController(int vid, int pid, String serial) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int tdSensor(Pointer protocol, int protocolLen, Pointer model, int modelLen, IntByReference id, IntByReference dataTypes) {
        if (tdSensorCounter == sensors.size()) {
            tdSensorCounter = 0;
            return -1;
        }

        SensorMock sensor = sensors.get(tdSensorCounter++);

        protocol.setString(0, sensor.protocol);
        model.setString(0, sensor.model);
        id.setValue(sensor.id);
        dataTypes.setValue(sensor.dataTypes);

        return TELLSTICK_SUCCESS;
    }

    @Override
    public int tdSensorValue(String protocol, String model, int id, int dataType, Pointer value, int len, IntByReference timestamp) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int tdController(IntByReference controllerId, IntByReference controllerType, Pointer name, int nameLen, IntByReference available) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int tdControllerValue(int controllerId, String name, Pointer value, int valueLen) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int tdSetControllerValue(int controllerId, String name, String value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int tdRemoveController(int controllerId) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void addSensor(int id, int dataTypes, String protocol, String model) {
        sensors.add(new SensorMock(id, dataTypes, protocol, model));
    }

    public void fireDeviceEvent(int deviceId, int method, String data) {
        deviceEventListeners.forEach((callbackId, listener) ->
                listener.event(deviceId, method, data, callbackId, null));
    }

    public void fireDeviceChangeEvent(int deviceId, int changeEvent, int changeType) {
        deviceChangeEventListeners.forEach((callbackId, listener) ->
                listener.event(deviceId, changeEvent, changeType, callbackId, null));
    }

    public void fireRawDeviceEvent(String data, int controllerId) {
        rawDeviceEventListeners.forEach((callbackId, listener) ->
                listener.event(data, controllerId, callbackId, null));
    }

    public void fireSensorEvent(String protocol, String model, int id, int dataType, String value, int timestamp) {
        sensorEventListeners.forEach((callbackId, listener) ->
                listener.event(protocol, model, id, dataType, value, timestamp, callbackId, null));
    }

    public void fireControllerEvent(int controllerId, int changeEvent, int changeType, String newValue) {
        controllerEventListeners.forEach((callbackId, listener) ->
                listener.event(controllerId, changeEvent, changeType, newValue, controllerId, null));
    }

    public class DeviceMock {
        public final int id;
        public int type = 0;
        public int methods = 0;
        public String name;
        public String protocol;
        public String model;
        public Map<String, String> parameter = new LinkedHashMap<>();

        public boolean on;
        public int dim = 0;
        public boolean up;

        DeviceMock(int id) {
            this.id = id;
        }
    }

    public class SensorMock {
        public final int id;
        public final int dataTypes;
        public final String protocol;
        public final String model;

        public SensorMock(int id, int dataTypes, String protocol, String model) {
            this.id = id;
            this.dataTypes = dataTypes;
            this.protocol = protocol;
            this.model = model;
        }
    }
}
