package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDDeviceChangeEvent;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDDeviceEvent;
import com.eitraz.tellstick.core.TellstickException;
import com.eitraz.tellstick.core.device.impl.*;
import com.eitraz.tellstick.core.util.Runner;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class DeviceHandler {
    private static final Logger logger = Logger.getLogger(DeviceHandler.class);

    private final Set<DeviceEventListener> deviceEventListeners = new CopyOnWriteArraySet<>();
    private final Map<Integer, Device> devices = new ConcurrentHashMap<>();

    private final TellstickCoreLibrary library;
    private final int supportedMethods;

    private final Runner eventRunner;

    private int deviceEventCallbackId = -1;
    private int deviceChangeEventCallbackId = -1;

    @SuppressWarnings("FieldCanBeLocal")
    private TDDeviceEventListener deviceEventListener;
    @SuppressWarnings("FieldCanBeLocal")
    private TDDeviceChangeEventListener deviceChangeEventListener;

    /**
     * @param library          tellstick core library
     * @param supportedMethods supported methods
     */
    public DeviceHandler(TellstickCoreLibrary library, int supportedMethods) {
        this.library = library;
        this.supportedMethods = supportedMethods;
        this.eventRunner = new Runner();
    }

    /**
     * Add Device Event Listen
     *
     * @param listener device event listener
     */
    public void addDeviceEventListener(DeviceEventListener listener) {
        deviceEventListeners.add(listener);
    }

    /**
     * Remove Device Event Listener
     *
     * @param listener device event listener
     */
    public void removeDeviceEventListener(DeviceEventListener listener) {
        deviceEventListeners.remove(listener);
    }

    /**
     * Start
     */
    public void start() {
        // Device Event Listener
        logger.debug("Starting Device Event Listener");
        deviceEventListener = new TDDeviceEventListener();
        deviceEventCallbackId = library.tdRegisterDeviceEvent(deviceEventListener, null);

        // Device Change Event Listener
        logger.debug("Starting Device Change Event Listener");
        deviceChangeEventListener = new TDDeviceChangeEventListener();
        deviceChangeEventCallbackId = library.tdRegisterDeviceChangeEvent(deviceChangeEventListener, null);

        // Start event runner
        eventRunner.start();
    }

    /**
     * Stop
     */
    public void stop() {
        // Stop event runner
        eventRunner.stop();

        // Stop Device Event Listener
        if (deviceEventCallbackId != -1) {
            logger.debug("Stopping Device Event Listener");
            library.tdUnregisterCallback(deviceEventCallbackId);
            deviceEventCallbackId = -1;
        }

        // Stop Device Change Event Listener
        if (deviceChangeEventCallbackId != -1) {
            logger.debug("Stopping Device Change Event Listener");
            library.tdUnregisterCallback(deviceChangeEventCallbackId);
            deviceChangeEventCallbackId = -1;
        }
    }

    /**
     * Get Devices
     *
     * @return devices
     */
    public List<Device> getDevices() {
        logger.trace("Get devices");

        List<Device> devices = new ArrayList<>();

        logger.trace("Get number of devices");
        int numDevices = library.tdGetNumberOfDevices();
        for (int i = 0; i < numDevices; i++) {
            logger.trace("Get device id " + i);

            int deviceId = library.tdGetDeviceId(i);

            try {
                devices.add(getDevice(deviceId));
            } catch (DeviceNotSupportedException e) {
                logger.warn("Device #" + deviceId + " is not supported.");
            }
        }

        return devices;
    }

    /**
     * Get Device
     *
     * @param deviceId device ID
     * @return device
     * @throws DeviceNotSupportedException
     */
    public Device getDevice(int deviceId) throws DeviceNotSupportedException {
        Device device = devices.get(deviceId);
        if (device == null) {
            device = createDevice(deviceId);
            devices.put(deviceId, device);
        }
        return device;
    }

    /**
     * Create Device
     *
     * @param deviceId device ID
     * @return device
     * @throws DeviceNotSupportedException
     */
    protected Device createDevice(int deviceId) throws DeviceNotSupportedException {
        logger.trace("Create device " + deviceId);
        int methods = library.tdMethods(deviceId, getSupportedMethods());

        logger.trace("Get device type " + deviceId);
        int type = library.tdGetDeviceType(deviceId);

        // Group Device
        if (type == TellstickCoreLibrary.TELLSTICK_TYPE_GROUP) {
            return new GroupDeviceImpl(this, deviceId);
        }

        // Scene Device
        else if (type == TellstickCoreLibrary.TELLSTICK_TYPE_SCENE) {
            return new SceneDeviceImpl(this, deviceId);
        }

        // Bell Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_BELL) > 0) {
            return new BellDeviceImpl(this, deviceId);
        }

        // Dimmable Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_DIM) > 0) {
            return new DimmableDeviceImpl(this, deviceId);
        }

        // Up / Down Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_UP) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_DOWN) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_STOP) > 0) {
            return new UpDownDeviceImpl(this, deviceId);
        }

        // On / Off Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_TURNON) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_TURNOFF) > 0) {
            return new OnOffDeviceImpl(this, deviceId);
        }

        // Scene Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_EXECUTE) > 0) {
            return new SceneDeviceImpl(this, deviceId);
        }

        // Not supported
        else {
            throw new DeviceNotSupportedException("The device is not supported");
        }
    }

    /**
     * Create device
     *
     * @param name       name
     * @param model      model
     * @param protocol   protocol
     * @param parameters parameters
     * @return device
     * @throws TellstickException
     * @throws DeviceNotSupportedException
     */
    public Device createDevice(String name, String model, String protocol, Map<String, String> parameters) throws TellstickException, DeviceNotSupportedException {
        logger.trace("Create add device");

        int deviceId = library.tdAddDevice();

        // Unable to create device
        if (deviceId <= 0)
            throw new TellstickException("Unable to create device,", library, deviceId);

        // Set Name
        if (!library.tdSetName(deviceId, name))
            logger.error("Unable to set device name");

        // Set Model
        if (!library.tdSetModel(deviceId, model))
            logger.error("Unable to set device model");

        // Set Protocol
        if (!library.tdSetProtocol(deviceId, protocol))
            logger.error("Unable to set device protocol");

        // Set Parameters
        for (Entry<String, String> entry : parameters.entrySet()) {
            // Set parameter
            if (!library.tdSetDeviceParameter(deviceId, entry.getKey(), entry.getValue()))
                logger.error("Unable to set parameter '" + entry.getKey());
        }

        return getDevice(deviceId);
    }

    /**
     * Remove device
     *
     * @param deviceId device ID
     * @return true if device was removed
     */
    public boolean removeDevice(int deviceId) {
        return library.tdRemoveDevice(deviceId);
    }

    /**
     * Remove device
     *
     * @param device device
     * @return true if device was removed
     */
    public boolean removeDevice(Device device) {
        return removeDevice(device.getDeviceId());
    }

    /**
     * @return the supportedMethods
     */
    public int getSupportedMethods() {
        return supportedMethods;
    }

    /**
     * @return the library
     */
    public TellstickCoreLibrary getLibrary() {
        return library;
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     * @param device   device
     */
    private void fireDeviceChanged(final int deviceId, final Device device) {
        eventRunner.offer(new Runnable() {
            @Override
            public void run() {
                for (DeviceEventListener listener : deviceEventListeners) {
                    listener.deviceChanged(deviceId, device);
                }
            }
        });
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     * @param device   device
     */
    private void fireDeviceAdded(final int deviceId, final Device device) {
        eventRunner.offer(new Runnable() {
            @Override
            public void run() {
                for (DeviceEventListener listener : deviceEventListeners) {
                    listener.deviceAdded(deviceId, device);
                }
            }
        });
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     */
    private void fireDeviceRemoved(final int deviceId) {
        eventRunner.offer(new Runnable() {
            @Override
            public void run() {
                for (DeviceEventListener listener : deviceEventListeners) {
                    listener.deviceRemoved(deviceId);
                }
            }
        });
    }

    /**
     * Device Event Listener
     */
    private class TDDeviceEventListener implements TDDeviceEvent {
        private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

        @Override
        public void event(int deviceId, int method, String data, int callbackId, Pointer context) {
            if (logger.isTraceEnabled())
                logger.trace(String.format("Event: %d, %d", deviceId, method));

            // String data = dataPointer.getString(0);

            // Don't fire event to often
            if (!timeoutHandler.isReady(String.format("%d,%d,%s", deviceId, method, data)))
                return;

            // Debug log
            if (logger.isDebugEnabled())
                logger.debug(String.format("DeviceId=%d, method=%d, data=%s", deviceId, method, data));

            try {
                fireDeviceChanged(deviceId, getDevice(deviceId));
            } catch (DeviceNotSupportedException e) {
                logger.warn(String.format("Device #%d is not supported.", deviceId));
            }
        }
    }

    /**
     * Device Change Event Listener
     */
    private class TDDeviceChangeEventListener implements TDDeviceChangeEvent {
        private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

        @Override
        public void event(int deviceId, int changeEvent, int changeType, int callbackId, Pointer context) {
            if (logger.isTraceEnabled())
                logger.trace(String.format("Event: %d", deviceId));

            // Don't fire event to often
            if (!timeoutHandler.isReady(String.format("%d,%d,%d", deviceId, changeEvent, changeType)))
                return;

            // Debug log
            if (logger.isDebugEnabled())
                logger.debug(String.format("DeviceId=%d, changeEvent=%d, changeType=%d", deviceId, changeEvent, changeType));

            // Device Changed
            if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_CHANGED || changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_STATE_CHANGED) {
                try {
                    fireDeviceChanged(deviceId, getDevice(deviceId));
                } catch (DeviceNotSupportedException e) {
                    logger.warn(String.format("Device #%d is not supported.", deviceId));
                }
            }
            // Device Added
            else if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_ADDED) {
                try {
                    fireDeviceAdded(deviceId, getDevice(deviceId));
                } catch (DeviceNotSupportedException e) {
                    logger.warn(String.format("Device #%d is not supported.", deviceId));
                }
            }
            // Device Removed
            else if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_REMOVED) {
                fireDeviceRemoved(deviceId);
            }
            // Unhandled event
            else {
                logger.error(String.format("Unhandled Device Change Event '%d'", changeEvent));
            }
        }
    }
}
