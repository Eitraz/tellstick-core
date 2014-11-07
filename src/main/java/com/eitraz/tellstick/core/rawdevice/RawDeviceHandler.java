package com.eitraz.tellstick.core.rawdevice;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDRawDeviceEvent;
import com.eitraz.tellstick.core.util.Runner;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class RawDeviceHandler {
    private static final Logger logger = Logger.getLogger(RawDeviceHandler.class);

    private static final int TIMEOUT = 5000;

    public static final String DELIMITER_MAJOR = ";";
    public static final String DELIMITER_MINOR = ":";

    public static final String _CLASS = "class";
    public static final String PROTOCOL = "protocol";
    public static final String MODEL = "model";

    public static final String COMMAND = "command";
    public static final String SENSOR = "sensor";

    public static final String ID = "id";
    public static final String METHOD = "method";

    public static final String HOUSE = "house";
    public static final String UNIT = "unit";
    public static final String GROUP = "group";

    public static final String BELL = "bell";
    public static final String ON = "turnon";
    public static final String OFF = "turnoff";

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
     * Get Raw Device
     *
     * @param parameters parameters
     * @return raw device
     */
    private RawDevice getRawDevice(Map<String, String> parameters) {
        String _class = parameters.remove(_CLASS);
        String protocol = parameters.remove(PROTOCOL);
        String model = parameters.remove(MODEL);

        // Command
        if (COMMAND.equalsIgnoreCase(_class)) {
            String method = parameters.get(METHOD);
            return new RawCommandDevice(_class, protocol, model, method, parameters);
        }
        // Sensor
        else if (SENSOR.equalsIgnoreCase(_class)) {
            String id = parameters.get(ID);
            return new RawSensorDevice(_class, protocol, model, id, parameters);
        }
        // Other
        else
            return new RawDevice(_class, protocol, model, parameters);
    }

    /**
     * Fire Raw Device Event
     *
     * @param device device
     */
    private void fireRawDeviceEvent(final RawDevice device) {
        eventRunner.offer(new Runnable() {
            @Override
            public void run() {
                for (RawDeviceEventListener listener : rawDeviceEventListeners) {
                    listener.rawDeviceEvent(device);
                }
            }
        });
    }

    /**
     * @param controllerId controller id
     * @param data         data
     */
    public void handleEvent(int controllerId, String data) {
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
        fireRawDeviceEvent(getRawDevice(parameters));
    }

    /**
     * Raw Device Event Listener
     */
    private class TDRawDeviceEventListener implements TDRawDeviceEvent {
        @Override
        public void event(String data, int controllerId, int callbackId, Pointer context) {
            logger.trace("event: " + data);

            handleEvent(controllerId, data);
        }

    }

}
