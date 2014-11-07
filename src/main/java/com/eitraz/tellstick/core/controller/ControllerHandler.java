package com.eitraz.tellstick.core.controller;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDControllerEvent;
import com.eitraz.tellstick.core.util.Runner;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ControllerHandler {
    private static final Logger logger = Logger.getLogger(ControllerHandler.class);

    private final Set<ControllerEventListener> controllerEventListeners = new CopyOnWriteArraySet<>();

    private final TellstickCoreLibrary library;

    private final Runner eventRunner;

    private int controllerEventCallbackId = -1;

    public ControllerHandler(TellstickCoreLibrary library) {
        this.library = library;
        this.eventRunner = new Runner();
    }

    /**
     * Add Sensor Event Listen
     *
     * @param listener
     */
    public void addDeviceEventListener(ControllerEventListener listener) {
        controllerEventListeners.add(listener);
    }

    /**
     * Remove Sensor Event Listener
     *
     * @param listener
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

        // Start Event Runner
        eventRunner.start();
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop Event Runner
        eventRunner.stop();

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

            eventRunner.offer(new Runnable() {
                @Override
                public void run() {
                    for (ControllerEventListener listener : controllerEventListeners) {
                        // TODO
                    }
                }
            });

            logger.info(string);
        }
    }
}
