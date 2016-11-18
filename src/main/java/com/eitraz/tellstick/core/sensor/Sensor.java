package com.eitraz.tellstick.core.sensor;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.sensor.value.SensorValue;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class Sensor<T extends SensorValue> {
    //	protocol: fineoffset, model: temperature, id: 199, dataType: 1, value: 11.3, timestamp: 1380376371, callbackId: 4

    private final SensorHandler sensorHandler;
    private final String protocol;
    private final String model;
    private final int id;
    private final int dataType;

    public Sensor(SensorHandler sensorHandler, int id, String protocol, String model, int dataType) {
        this.sensorHandler = sensorHandler;
        this.protocol = protocol;
        this.model = model;
        this.id = id;
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "Sensor [protocol=" + protocol + ", model=" + model + ", id=" + id + ", dataType=" + dataType + "]";
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the dataType
     */
    public int getDataType() {
        return dataType;
    }

    public SensorHandler getSensorHandler() {
        return sensorHandler;
    }

    /**
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public T getValue() throws SensorException {
        int valueLen = 128;
        Pointer valuePointer = new Memory(valueLen);
        IntByReference timestamp = new IntByReference();

        int result = sensorHandler.getLibrary().tdSensorValue(protocol, model, id, dataType, valuePointer, valueLen, timestamp);
        if (result != TellstickCoreLibrary.TELLSTICK_SUCCESS) {
            throw new SensorException(this, result);
        }

        return SensorValue.parseSensorValue(dataType, timestamp.getValue(), valuePointer.getString(0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sensor sensor = (Sensor) o;

        return id == sensor.id && dataType == sensor.dataType;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + dataType;
        return result;
    }
}
