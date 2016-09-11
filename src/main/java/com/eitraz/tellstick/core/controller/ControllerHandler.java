package com.eitraz.tellstick.core.controller;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDControllerEvent;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ControllerHandler {
    private static final Logger logger = Logger.getLogger(ControllerHandler.class);

    private final Set<ControllerEventListener> controllerEventListeners = new CopyOnWriteArraySet<>();

    private final TellstickCoreLibrary library;

    private final Executor executor = Executors.newFixedThreadPool(4);

    private int controllerEventCallbackId = -1;

    public ControllerHandler(TellstickCoreLibrary library) {
        this.library = library;
    }

    /**
     * Add Sensor Event Listen
     */
    public void addDeviceEventListener(ControllerEventListener listener) {
        controllerEventListeners.add(listener);
    }

    /**
     * Remove Sensor Event Listener
     */
    public void removeDeviceEventListener(ControllerEventListener listener) {
        controllerEventListeners.remove(listener);
    }

    /**
     * Start
     */
    public void start() {
        // Controller Event Listener
        logger.debug("Starting Controller Event Listener");
        TDControllerEventListener controllerEventListener = new TDControllerEventListener();
        controllerEventCallbackId = library.tdRegisterControllerEvent(controllerEventListener, null);
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop Controller Event Listener
        if (controllerEventCallbackId != -1) {
            logger.debug("Stopping Controller Event Listener");
            library.tdUnregisterCallback(controllerEventCallbackId);
            controllerEventCallbackId = -1;
        }
    }

    /**
     * Sensor Event Listener
     */
    private class TDControllerEventListener implements TDControllerEvent {
        public void event(int controllerId, int changeEvent, int changeType, String newValue, int callbackId, Pointer context) {
            String string = "";
            string += "controllerId: " + controllerId + ", ";
            string += "changeEvent: " + changeEvent + ", ";
            string += "newValue: " + newValue + ", ";
            string += "callbackId: " + callbackId;

            executor.execute(() -> {
                for (ControllerEventListener ignored : controllerEventListeners) {
                    // TODO
                }
            });

            logger.info(string);
        }
    }
}
