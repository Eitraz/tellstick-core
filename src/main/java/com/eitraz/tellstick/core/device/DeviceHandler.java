package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickException;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Pointer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("WeakerAccess")
public class DeviceHandler {
    private static final Logger logger = LogManager.getLogger();

    private static final Duration TIMEOUT = Duration.ofSeconds(1);

    private final Set<DeviceEventListener> deviceEventListeners = new CopyOnWriteArraySet<>();
    private final Set<DeviceChangeEventListener> deviceChangeEventListeners = new CopyOnWriteArraySet<>();

    private final TellstickCoreLibrary library;
    private final int supportedMethods;

    private final Executor executor = Executors.newFixedThreadPool(1);

    private int deviceEventCallbackId = -1;
    private int deviceChangeEventCallbackId = -1;

    @SuppressWarnings("FieldCanBeLocal")
    private DeviceHandler.TDDeviceEventListener deviceEventListener;
    @SuppressWarnings("FieldCanBeLocal")
    private DeviceHandler.TDDeviceChangeEventListener deviceChangeEventListener;

    /**
     * @param library          tellstick core library
     * @param supportedMethods supported methods
     */
    public DeviceHandler(TellstickCoreLibrary library, int supportedMethods) {
        this.library = library;
        this.supportedMethods = supportedMethods;
    }

    public void addDeviceEventListener(DeviceEventListener listener) {
        deviceEventListeners.add(listener);
    }

    public void removeDeviceEventListener(DeviceEventListener listener) {
        deviceEventListeners.remove(listener);
    }

    public void addDeviceChangeEventListener(DeviceChangeEventListener listener) {
        deviceChangeEventListeners.add(listener);
    }

    public void removeDeviceChangeEventListener(DeviceChangeEventListener listener) {
        deviceChangeEventListeners.remove(listener);
    }

    public void start() {
        // Device Event Listener
        logger.debug("Starting Device Event Listener");
        deviceEventListener = new DeviceHandler.TDDeviceEventListener();
        deviceEventCallbackId = library.tdRegisterDeviceEvent(deviceEventListener, null);

        // Device Change Event Listener
        logger.debug("Starting Device Change Event Listener");
        deviceChangeEventListener = new DeviceHandler.TDDeviceChangeEventListener();
        deviceChangeEventCallbackId = library.tdRegisterDeviceChangeEvent(deviceChangeEventListener, null);
    }

    public void stop() {
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

    public List<Device> getDevices() {
        logger.trace("Get devices");

        List<Device> devices = new ArrayList<>();

        logger.trace("Get number of devices");
        int numDevices = library.tdGetNumberOfDevices();
        for (int i = 0; i < numDevices; i++) {
            logger.trace("Get device id {}", i);

            int deviceId = library.tdGetDeviceId(i);

            try {
                devices.add(getDevice(deviceId));
            } catch (DeviceNotSupportedException e) {
                logger.warn("Device #{} is not supported.", deviceId, e);
            }
        }

        return devices;
    }

    public <T extends Device> T getDevice(int deviceId) throws DeviceNotSupportedException {
        return createDevice(deviceId);
    }

    @SuppressWarnings("unchecked")
    public <T extends Device> Optional<T> getDeviceByName(String name) {
        return getDevices().stream()
                .filter(d -> name.equals(d.getName()))
                .map(d -> (T)d)
                .findFirst();
    }

    /**
     * Create Device
     *
     * @param deviceId device ID
     * @return device
     */
    @SuppressWarnings("unchecked")
    private <T extends Device> T createDevice(int deviceId) throws DeviceNotSupportedException {
        logger.trace("Create device {}", deviceId);
        int methods = library.tdMethods(deviceId, getSupportedMethods());

        logger.trace("Get device type {}", deviceId);
        int type = library.tdGetDeviceType(deviceId);

        // Group Device
        if (type == TellstickCoreLibrary.TELLSTICK_TYPE_GROUP) {
            return (T) new GroupDevice(this, deviceId);
        }

        // Scene Device
        else if (type == TellstickCoreLibrary.TELLSTICK_TYPE_SCENE) {
            return (T) new SceneDevice(this, deviceId);
        }

        // Bell Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_BELL) > 0) {
            return (T) new BellDevice(this, deviceId);
        }

        // Dimmable Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_DIM) > 0) {
            return (T) new DimmableDevice(this, deviceId);
        }

        // Up / Down Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_UP) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_DOWN) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_STOP) > 0) {
            return (T) new UpDownDevice(this, deviceId);
        }

        // On / Off Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_TURNON) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_TURNOFF) > 0) {
            return (T) new OnOffDevice(this, deviceId);
        }

        // Scene Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_EXECUTE) > 0) {
            return (T) new SceneDevice(this, deviceId);
        }

        // Not supported
        else {
            throw new DeviceNotSupportedException("The device is not supported");
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Device> T createDevice(String name, String model, String protocol, Map<String, String> parameters) throws TellstickException, DeviceNotSupportedException {
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
        parameters.entrySet().forEach(entry -> {
            if (!library.tdSetDeviceParameter(deviceId, entry.getKey(), entry.getValue()))
                logger.error("Unable to set parameter '{}'", entry.getKey());
        });

        return (T) getDevice(deviceId);
    }

    public boolean removeDevice(int deviceId) {
        return library.tdRemoveDevice(deviceId);
    }

    public boolean removeDevice(Device device) {
        return removeDevice(device.getDeviceId());
    }

    public int getSupportedMethods() {
        return supportedMethods;
    }

    public TellstickCoreLibrary getLibrary() {
        return library;
    }

    /**
     * Fire device Event
     *
     * @param deviceId device ID
     * @param device   device
     * @param method   The new device state. Can be {@link TellstickCoreLibrary#TELLSTICK_TURNON}, {@link TellstickCoreLibrary#TELLSTICK_TURNOFF}, etc
     * @param data     If method is {@link TellstickCoreLibrary#TELLSTICK_DIM} this holds the current value as human readable string, example "128" or 50%
     */
    private void fireDeviceEvent(final int deviceId, final Device device, final int method, final String data) {
        executor.execute(() -> deviceEventListeners.forEach(listener -> listener.deviceEvent(deviceId, device, method, data)));
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId   device ID
     * @param device     device
     * @param changeType Indicates what has changed  (e.g {@link TellstickCoreLibrary#TELLSTICK_CHANGE_NAME}, {@link TellstickCoreLibrary#TELLSTICK_CHANGE_PROTOCOL}, {@link TellstickCoreLibrary#TELLSTICK_CHANGE_MODEL} or {@link TellstickCoreLibrary#TELLSTICK_CHANGE_METHOD})
     */
    private void fireDeviceChanged(final int deviceId, final Device device, final int changeType) {
        executor.execute(() -> deviceChangeEventListeners.forEach(listener -> listener.deviceChanged(deviceId, device, changeType)));
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     * @param device   device
     */
    private void fireDeviceAdded(final int deviceId, final Device device) {
        executor.execute(() -> deviceChangeEventListeners.forEach(listener -> listener.deviceAdded(deviceId, device)));
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     */
    private void fireDeviceRemoved(final int deviceId) {
        executor.execute(() -> deviceChangeEventListeners.forEach(listener -> listener.deviceRemoved(deviceId)));
    }

    /**
     * Device Event Listener
     */
    private class TDDeviceEventListener implements TellstickCoreLibrary.TDDeviceEvent {
        private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

        @Override
        public void event(int deviceId, int method, String data, int callbackId, Pointer context) {
            logger.trace("Event: {}, {}", deviceId, method);

            // String data = dataPointer.getString(0);

            // Don't fire event to often
            if (!timeoutHandler.isReady(String.format("%d,%d,%s", deviceId, method, data), TIMEOUT))
                return;

            // Debug log
            logger.debug("Event: DeviceId={}, method={}, data={}", deviceId, method, data);

            try {
                fireDeviceEvent(deviceId, getDevice(deviceId), method, data);
            } catch (DeviceNotSupportedException e) {
                logger.warn("Device #{} is not supported.", deviceId);
            }
        }
    }

    /**
     * Device Change Event Listener
     */
    private class TDDeviceChangeEventListener implements TellstickCoreLibrary.TDDeviceChangeEvent {
        private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

        @Override
        public void event(int deviceId, int changeEvent, int changeType, int callbackId, Pointer context) {
            logger.trace("ChangeEvent: {}", deviceId);

            // Don't fire event to often
            if (!timeoutHandler.isReady(String.format("%d,%d,%d", deviceId, changeEvent, changeType), TIMEOUT))
                return;

            // Debug log
            logger.debug("ChangeEvent: DeviceId={}, changeEvent={}, changeType={}", deviceId, changeEvent, changeType);

            // Device Changed
            if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_CHANGED || changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_STATE_CHANGED) {
                try {
                    fireDeviceChanged(deviceId, getDevice(deviceId), changeType);
                } catch (DeviceNotSupportedException e) {
                    logger.warn("Device #{} is not supported.", deviceId, e);
                }
            }
            // Device Added
            else if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_ADDED) {
                try {
                    fireDeviceAdded(deviceId, getDevice(deviceId));
                } catch (DeviceNotSupportedException e) {
                    logger.warn("Device #{} is not supported.", deviceId, e);
                }
            }
            // Device Removed
            else if (changeEvent == TellstickCoreLibrary.TELLSTICK_DEVICE_REMOVED) {
                fireDeviceRemoved(deviceId);
            }
            // Unhandled event
            else {
                logger.error("Unhandled Device Change Event '{}'", changeEvent);
            }
        }
    }
}
