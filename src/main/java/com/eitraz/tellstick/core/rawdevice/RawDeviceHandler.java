package com.eitraz.tellstick.core.rawdevice;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDRawDeviceEvent;
import com.eitraz.tellstick.core.rawdevice.events.*;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Pointer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RawDeviceHandler {
    private static final Logger logger = LogManager.getLogger();

    private static final Duration TIMEOUT = Duration.ofSeconds(1);

    private static final String DELIMITER_MAJOR = ";";
    private static final String DELIMITER_MINOR = ":";

    private final Set<RawDeviceEventListener> rawDeviceEventListeners = new CopyOnWriteArraySet<>();

    private final TellstickCoreLibrary library;

    private final Executor executor = Executors.newFixedThreadPool(1);

    private int rawDeviceEventCallbackId = -1;
    @SuppressWarnings("FieldCanBeLocal")
    private TDRawDeviceEventListener rawDeviceEventListener;

    private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

    public RawDeviceHandler(TellstickCoreLibrary library) {
        this.library = library;
    }

    /**
     * Add Raw Device Event Listen
     *
     * @param listener raw device event listener
     */
    public void addRawDeviceEventListener(RawDeviceEventListener listener) {
        rawDeviceEventListeners.add(listener);
    }

    /**
     * Remove Raw Device Event Listener
     *
     * @param listener raw device event listener
     */
    public void removeRawDeviceEventListener(RawDeviceEventListener listener) {
        rawDeviceEventListeners.remove(listener);
    }

    /**
     * Start
     */
    public void start() {
        // Raw Device Event Listener
        logger.debug("Starting Raw Device Event Listener");
        rawDeviceEventListener = new TDRawDeviceEventListener();
        rawDeviceEventCallbackId = library.tdRegisterRawDeviceEvent(rawDeviceEventListener, null);
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop Raw Device Event Listener
        if (rawDeviceEventCallbackId != -1) {
            logger.debug("Stopping Raw Device Event Listener");
            library.tdUnregisterCallback(rawDeviceEventCallbackId);
            rawDeviceEventCallbackId = -1;
        }
    }

    /**
     * Fire Raw Device Event
     *
     * @param parameters parameters
     */
    private void fireRawDeviceEvent(final Map<String, String> parameters) {
        RawDeviceEvent event = createEvent(parameters);

        executor.execute(() -> rawDeviceEventListeners.forEach(listener -> listener.rawDeviceEvent(event)));
    }

    private RawDeviceEvent createEvent(Map<String, String> parameters) {
        String _class = parameters.get(RawDeviceEvent._CLASS);
        String method = parameters.get(RawDeviceEvent.METHOD);

        // Command
        if (RawDeviceEvent.COMMAND.equalsIgnoreCase(_class)) {
            // On/Off command
            if (RawDeviceEvent.ON.equalsIgnoreCase(method) || RawDeviceEvent.OFF.equalsIgnoreCase(method)) {
                return new RawOnOffCommandEvent(parameters);
            }
            // Other
            else {
                return new RawCommandEvent(parameters);
            }
        }
        // Sensor
        else if (RawDeviceEvent.SENSOR.equalsIgnoreCase(_class)) {
            // Temperature and humidity
            if (parameters.get(RawDeviceEvent.TEMP) != null && parameters.get(RawDeviceEvent.HUMIDITY) != null) {
                return new RawTemperatureHumiditySensorEvent(parameters);
            }
            // Temperature
            else if (parameters.get(RawDeviceEvent.TEMP) != null) {
                return new RawTemperatureSensorEvent(parameters);
            }
            // Humidity
            else if (parameters.get(RawDeviceEvent.HUMIDITY) != null) {
                return new RawHumiditySensorEvent(parameters);
            }
            // Other
            else {
                return new RawSensorEvent(parameters);
            }
        }
        // Other
        else {
            return new RawDeviceEvent(parameters);
        }
    }

    /**
     * @param controllerId controller id
     * @param data         data
     */
    private void handleEvent(int controllerId, String data) {
        // String data = dataPointer.getString(0);

        // Don't fire event to often
        if (!timeoutHandler.isReady(data, TIMEOUT))
            return;

        // Debug log
        logger.debug(data);

        String[] split = data.split(DELIMITER_MAJOR);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("controllerId", Integer.toString(controllerId));
        for (String string : split) {
            String[] pair = string.split(DELIMITER_MINOR);
            if (pair.length == 2)
                parameters.put(pair[0].trim(), pair[1].trim());
        }

        // Fire event
        fireRawDeviceEvent(parameters);
    }

    /**
     * Raw Device Event Listener
     */
    private class TDRawDeviceEventListener implements TDRawDeviceEvent {
        @Override
        public void event(String data, int controllerId, int callbackId, Pointer context) {
            logger.trace("event: {}", () -> data);

            handleEvent(controllerId, data);
        }
    }

}
