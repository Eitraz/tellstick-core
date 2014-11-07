package com.eitraz.tellstick.core.sensor;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDSensorEvent;
import com.eitraz.tellstick.core.util.Runner;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SensorHandler {
    private static final Logger logger = Logger.getLogger(SensorHandler.class);

    private final int[] DATA_TYPES = new int[]{
            TellstickCoreLibrary.TELLSTICK_TEMPERATURE,
            TellstickCoreLibrary.TELLSTICK_HUMIDITY,
            TellstickCoreLibrary.TELLSTICK_RAINRATE,
            TellstickCoreLibrary.TELLSTICK_RAINTOTAL,
            TellstickCoreLibrary.TELLSTICK_WINDDIRECTION,
            TellstickCoreLibrary.TELLSTICK_WINDAVERAGE,
            TellstickCoreLibrary.TELLSTICK_WINDGUST,
    };

    private final Set<SensorEventListener> sensorEventListeners = new CopyOnWriteArraySet<>();

    private final TellstickCoreLibrary library;
    private final int supportedDataTypes;

    private final Runner eventRunner;

    private int sensorEventCallbackId = -1;
    @SuppressWarnings("FieldCanBeLocal")
    private TDSensorEventListener sensorEventListener;

    public SensorHandler(TellstickCoreLibrary library, int supportedDataTypes) {
        this.library = library;
        this.supportedDataTypes = supportedDataTypes;
        this.eventRunner = new Runner();
    }

    /**
     * @return the supportedDataTypes
     */
    public int getSupportedDataTypes() {
        return supportedDataTypes;
    }

    /**
     * Add Sensor Event Listen
     *
     * @param listener sensor event listener
     */
    public void addSensorEventListener(SensorEventListener listener) {
        sensorEventListeners.add(listener);
    }

    /**
     * Remove Sensor Event Listener
     *
     * @param listener sensor event listener
     */
    public void removeSensorEventListener(SensorEventListener listener) {
        sensorEventListeners.remove(listener);
    }

    /**
     * Start
     */
    public void start() {
        // Sensor Event Listener
        logger.debug("Starting Sensor Event Listener");
        sensorEventListener = new TDSensorEventListener();
        sensorEventCallbackId = library.tdRegisterSensorEvent(sensorEventListener, null);

        // Start Event Runner
        eventRunner.start();
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop Event Runner
        eventRunner.stop();

        // Stop Sensor Event Listener
        if (sensorEventCallbackId != -1) {
            logger.debug("Stopping Sensor Event Listener");
            library.tdUnregisterCallback(sensorEventCallbackId);
            sensorEventCallbackId = -1;
        }
    }

    /**
     * Get Sensor
     *
     * @param id       id
     * @param protocol protocol
     * @param model    model
     * @param dataType data type
     * @param value    value
     * @return sensor
     */
    private Sensor getSensor(int id, String protocol, String model, int dataType, String value, long timestamp) {
        if (dataType == TellstickCoreLibrary.TELLSTICK_TEMPERATURE)
            return new TemperatureSensor(id, protocol, model, value, timestamp);
        else
            return new Sensor(id, protocol, model, dataType, value, timestamp);
    }

    /**
     * Get Sensors
     *
     * @return list of sensors
     */
    public List<Sensor> getSensors() {
        List<Sensor> sensors = new ArrayList<>();

        int protocolLen = 128;
        Pointer protocolPointer = new Memory(protocolLen);
        int modelLen = 128;
        Pointer modelPointer = new Memory(modelLen);

        IntByReference idReference = new IntByReference();
        IntByReference dataTypesReference = new IntByReference();

        // Get all sensor
        while (library.tdSensor(protocolPointer, protocolLen, modelPointer, modelLen, idReference, dataTypesReference) == TellstickCoreLibrary.TELLSTICK_SUCCESS) {
            int id = idReference.getValue();
            String protocol = protocolPointer.getString(0);
            String model = modelPointer.getString(0);
            int dataTypes = dataTypesReference.getValue();

            int valueLen = 128;
            Pointer valuePointer = new Memory(valueLen);
            IntByReference timestamp = new IntByReference();

            // Get data types
            for (int dataType : getDataTypes(dataTypes)) {
                if (library.tdSensorValue(protocol, model, id, dataType, valuePointer, valueLen, timestamp) == TellstickCoreLibrary.TELLSTICK_SUCCESS) {
                    String value = valuePointer.getString(0);
                    sensors.add(getSensor(id, protocol, model, dataType, value, timestamp.getValue()));
                }
            }

        }

        return sensors;
    }

    /**
     * Get data types from datatypes
     *
     * @param dataTypes data types
     * @return data types
     */
    private List<Integer> getDataTypes(int dataTypes) {
        List<Integer> result = new ArrayList<>();

        for (int dataType : DATA_TYPES) {
            if ((getSupportedDataTypes() & dataType) != 0 && (dataTypes & dataType) != 0)
                result.add(dataType);
        }

        return result;
    }

    /**
     * Fire Sensor Event
     *
     * @param sensor sensor
     */
    private void fireSensorEvent(final Sensor sensor) {
        eventRunner.offer(new Runnable() {
            @Override
            public void run() {
                for (SensorEventListener listener : sensorEventListeners) {
                    listener.sensorEvent(sensor);
                }
            }
        });
    }

    /**
     * Sensor Event Listener
     */
    private class TDSensorEventListener implements TDSensorEvent {
        private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

        @Override
        public void event(String protocol, String model, int id, int dataType, String value, int timestamp, int callbackId, Pointer context) {
            String string = String.format(
                    "protocol: %s,model: %s, id: %d, dataType: %d, value: %s, timestamp: %d, callbackId: %d",
                    protocol, model, id, dataType, value, timestamp, callbackId);

            // Don't fire event to often
            if (!timeoutHandler.isReady(string))
                return;

            // Debug log
            logger.debug(string);

            // Fire sensor event
            fireSensorEvent(getSensor(id, protocol, model, dataType, value, timestamp));
        }

    }

}
