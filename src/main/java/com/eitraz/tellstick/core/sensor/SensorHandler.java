package com.eitraz.tellstick.core.sensor;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDSensorEvent;
import com.eitraz.tellstick.core.sensor.value.SensorValue;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SensorHandler {
    private static final Logger logger = LogManager.getLogger();

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

    private static final Duration TIMEOUT = Duration.ofSeconds(1);

    private final TellstickCoreLibrary library;
    private final int supportedDataTypes;

    private final Executor executor = Executors.newFixedThreadPool(1);

    private int sensorEventCallbackId = -1;
    @SuppressWarnings("FieldCanBeLocal")
    private TDSensorEventListener sensorEventListener;

    public SensorHandler(TellstickCoreLibrary library, int supportedDataTypes) {
        this.library = library;
        this.supportedDataTypes = supportedDataTypes;
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
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop Sensor Event Listener
        if (sensorEventCallbackId != -1) {
            logger.debug("Stopping Sensor Event Listener");
            library.tdUnregisterCallback(sensorEventCallbackId);
            sensorEventCallbackId = -1;
        }
    }

    public TellstickCoreLibrary getLibrary() {
        return library;
    }

    @SuppressWarnings("unchecked")
    public <T extends SensorValue> Optional<Sensor<T>> getSensor(int id, int dataType) {
        return getSensors().stream()
                .filter(s -> s.getId() == id && s.getDataType() == dataType)
                .map(s -> (Sensor<T>) s)
                .findFirst();
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

            getDataTypes(dataTypes)
                    .forEach(dataType -> {
                        sensors.add(new Sensor(this, id, protocol, model, dataType));
                    });
        }

        return sensors;
    }

    /**
     * Get data types from data types
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
    private void fireSensorEvent(final Sensor sensor, final SensorValue value) {
        executor.execute(() -> sensorEventListeners.forEach(listener -> listener.sensorEvent(sensor, value)));
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
            if (!timeoutHandler.isReady(string, TIMEOUT))
                return;

            // Debug log
            logger.debug(string);

            // Fire sensor event
            fireSensorEvent(new Sensor(SensorHandler.this, id, protocol, model, dataType),
                    SensorValue.parseSensorValue(dataType, timestamp, value));
        }
    }
}
