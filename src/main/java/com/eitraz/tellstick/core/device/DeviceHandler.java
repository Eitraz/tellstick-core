package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickException;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Pointer;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeviceHandler {
    private static final Logger logger = Logger.getLogger(DeviceHandler.class);

    private static final Duration TIMEOUT = Duration.ofSeconds(1);

    private final Set<DeviceEventListener> deviceEventListeners = new CopyOnWriteArraySet<>();

    private final TellstickCoreLibrary library;
    private final int supportedMethods;

    private final Executor executor = Executors.newFixedThreadPool(4);

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

    public Device getDevice(int deviceId) throws DeviceNotSupportedException {
        return createDevice(deviceId);
//        Device device = devices.get(deviceId);
//        if (device == null) {
//            device = createDevice(deviceId);
//            devices.put(deviceId, device);
//        }
//        return device;
    }

    /**
     * Create Device
     *
     * @param deviceId device ID
     * @return device
     * @throws DeviceNotSupportedException
     */
    private Device createDevice(int deviceId) throws DeviceNotSupportedException {
        logger.trace("Create device " + deviceId);
        int methods = library.tdMethods(deviceId, getSupportedMethods());

        logger.trace("Get device type " + deviceId);
        int type = library.tdGetDeviceType(deviceId);

        // Group Device
        if (type == TellstickCoreLibrary.TELLSTICK_TYPE_GROUP) {
            return new GroupDevice(this, deviceId);
        }

        // Scene Device
        else if (type == TellstickCoreLibrary.TELLSTICK_TYPE_SCENE) {
            return new SceneDevice(this, deviceId);
        }

        // Bell Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_BELL) > 0) {
            return new BellDevice(this, deviceId);
        }

        // Dimmable Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_DIM) > 0) {
            return new DimmableDevice(this, deviceId);
        }

        // Up / Down Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_UP) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_DOWN) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_STOP) > 0) {
            return new UpDownDevice(this, deviceId);
        }

        // On / Off Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_TURNON) > 0 &&
                (methods & TellstickCoreLibrary.TELLSTICK_TURNOFF) > 0) {
            return new OnOffDevice(this, deviceId);
        }

        // Scene Device
        else if ((methods & TellstickCoreLibrary.TELLSTICK_EXECUTE) > 0) {
            return new SceneDevice(this, deviceId);
        }

        // Not supported
        else {
            throw new DeviceNotSupportedException("The device is not supported");
        }
    }

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
        parameters.entrySet().stream()
                .filter(entry -> !library.tdSetDeviceParameter(deviceId, entry.getKey(), entry.getValue()))
                .forEach(entry -> logger.error(String.format("Unable to set parameter '%s'", entry.getKey())));

        return getDevice(deviceId);
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
     * Fire Device Changed
     *
     * @param deviceId device ID
     * @param device   device
     */
    private void fireDeviceChanged(final int deviceId, final Device device) {
        executor.execute(() -> deviceEventListeners.forEach(listener -> listener.deviceChanged(deviceId, device)));
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     * @param device   device
     */
    private void fireDeviceAdded(final int deviceId, final Device device) {
        executor.execute(() -> deviceEventListeners.forEach(listener -> listener.deviceAdded(deviceId, device)));
    }

    /**
     * Fire Device Changed
     *
     * @param deviceId device ID
     */
    private void fireDeviceRemoved(final int deviceId) {
        executor.execute(() -> deviceEventListeners.forEach(listener -> listener.deviceRemoved(deviceId)));
    }

    /**
     * Device Event Listener
     */
    private class TDDeviceEventListener implements TellstickCoreLibrary.TDDeviceEvent {
        private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

        @Override
        public void event(int deviceId, int method, String data, int callbackId, Pointer context) {
            if (logger.isTraceEnabled())
                logger.trace(String.format("Event: %d, %d", deviceId, method));

            // String data = dataPointer.getString(0);

            // Don't fire event to often
            if (!timeoutHandler.isReady(String.format("%d,%d,%s", deviceId, method, data), TIMEOUT))
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
    private class TDDeviceChangeEventListener implements TellstickCoreLibrary.TDDeviceChangeEvent {
        private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<>();

        @Override
        public void event(int deviceId, int changeEvent, int changeType, int callbackId, Pointer context) {
            if (logger.isTraceEnabled())
                logger.trace(String.format("Event: %d", deviceId));

            // Don't fire event to often
            if (!timeoutHandler.isReady(String.format("%d,%d,%d", deviceId, changeEvent, changeType), TIMEOUT))
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
