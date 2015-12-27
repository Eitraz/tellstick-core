package com.eitraz.tellstick.core.rawdevice;

import com.eitraz.library.Duration;
import com.eitraz.library.TimeoutHandler;
import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDRawDeviceEvent;
import com.eitraz.tellstick.core.rawdevice.events.RawDeviceEvent;
import com.eitraz.tellstick.core.rawdevice.events.impl.*;
import com.eitraz.tellstick.core.util.Runner;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class RawDeviceHandler {
    private static final Logger logger = Logger.getLogger(RawDeviceHandler.class);

    private static final Duration TIMEOUT = Duration.ONE_SECOND;

    private static final String DELIMITER_MAJOR = ";";
    private static final String DELIMITER_MINOR = ":";

    private final Set<RawDeviceEventListener> rawDeviceEventListeners = new CopyOnWriteArraySet<>();

    private final TellstickCoreLibrary library;

    private final Runner eventRunner;

    private int rawDeviceEventCallbackId = -1;
    @SuppressWarnings("FieldCanBeLocal")
    private TDRawDeviceEventListener rawDeviceEventListener;

    private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>(TIMEOUT);

    public RawDeviceHandler(TellstickCoreLibrary library) {
        this.library = library;
        this.eventRunner = new Runner();
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

        // Start Event Runner
        eventRunner.start();
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop Event Runner
        eventRunner.stop();

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

        eventRunner.offer(() -> rawDeviceEventListeners.forEach(listener -> listener.rawDeviceEvent(event)));
    }

    private RawDeviceEvent createEvent(Map<String, String> parameters) {
        String _class = parameters.get(RawDeviceEvent._CLASS);
        String method = parameters.get(RawDeviceEvent.METHOD);

        // Command
        if (RawDeviceEvent.COMMAND.equalsIgnoreCase(_class)) {
            // On/Off command
            if (RawDeviceEvent.ON.equalsIgnoreCase(method) || RawDeviceEvent.OFF.equalsIgnoreCase(method)) {
                return new RawOnOffCommandEventImpl(parameters);
            }
            // Other
            else {
                return new RawCommandEventImpl(parameters);
            }
        }
        // Sensor
        else if (RawDeviceEvent.SENSOR.equalsIgnoreCase(_class)) {
            // Temperature and humidity
            if (parameters.get(RawDeviceEvent.TEMP) != null && parameters.get(RawDeviceEvent.HUMIDITY) != null) {
                return new RawTemperatureHumiditySensorEventImpl(parameters);
            }
            // Temperature
            else if (parameters.get(RawDeviceEvent.TEMP) != null) {
                return new RawTemperatureSensorEventImpl(parameters);
            }
            // Humidity
            else if (parameters.get(RawDeviceEvent.HUMIDITY) != null) {
                return new RawHumiditySensorEventImpl(parameters);
            }
            // Other
            else {
                return new RawSensorEventImpl(parameters);
            }
        }
        // Other
        else {
            return new RawDeviceEventImpl(parameters);
        }
    }

    /**
     * @param controllerId controller id
     * @param data         data
     */
    private void handleEvent(int controllerId, String data) {
        // String data = dataPointer.getString(0);

        // Don't fire event to often
        if (!timeoutHandler.isReady(data))
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
            if (logger.isTraceEnabled())
                logger.trace(String.format("event: %s", data));

            handleEvent(controllerId, data);
        }
    }

}
