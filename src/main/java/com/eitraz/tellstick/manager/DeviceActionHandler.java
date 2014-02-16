package com.eitraz.tellstick.manager;

import org.apache.log4j.Logger;

import com.eitraz.tellstick.core.device.BellDevice;
import com.eitraz.tellstick.core.device.Device;
import com.eitraz.tellstick.core.device.DeviceException;
import com.eitraz.tellstick.core.device.DimmableDevice;
import com.eitraz.tellstick.core.device.GroupDevice;
import com.eitraz.tellstick.core.device.OnOffDevice;
import com.eitraz.tellstick.core.device.SceneDevice;
import com.eitraz.tellstick.core.device.UpDownDevice;

/**
 * Device Action Handler
 * 
 * @author Petter Alstermark
 *
 */
public final class DeviceActionHandler {
	private static final Logger logger = Logger.getLogger(DeviceActionHandler.class);

	/**
	 * Handle Device Action
	 * @param deviceAction
	 * @throws DeviceException
	 */
	public static void handleDeviceAction(DeviceAction deviceAction) throws DeviceException {
		Device device = deviceAction.getDevice();
		Object action = deviceAction.getAction();

		logger.trace(device + ", "+ action);

		// On/off
		if (device instanceof OnOffDevice)
			handleOnOffDevice((OnOffDevice)device, action);

		// Dimmable
		else if (device instanceof DimmableDevice)
			handleDimmableDevice((DimmableDevice)device, action);

		// Scene
		else if (device instanceof SceneDevice)
			handleSceneDevice((SceneDevice)device, action);

		// Up/down
		else if (device instanceof UpDownDevice)
			handleUpDownDevice((UpDownDevice)device, action);

		// Bell device
		else if (device instanceof BellDevice)
			handleBellDevice((BellDevice)device, action);

		// Group Device
		else if (device instanceof GroupDevice)
			handleGroupDevice((GroupDevice)device, action);

		// Unhandled Device
		else
			throw new IllegalArgumentException("Unhandled device " + device);
	}

	/**
	 * Handle Group Device
	 * @param device
	 * @param action
	 */
	private static void handleGroupDevice(GroupDevice device, Object action) {
		// On
		if (Boolean.TRUE.equals(action))
			device.on();
		// Off
		else if (Boolean.FALSE.equals(action))
			device.off();
		// Unhandled
		else
			throw new IllegalArgumentException(device + " can't handle action " + action);
	}

	/**
	 * Handle Bell Device
	 * @param device
	 * @param action
	 * @throws DeviceException
	 */
	private static void handleBellDevice(BellDevice device, Object action) throws DeviceException {
		device.bell();
	}

	/**
	 * Handle Up/Down Device
	 * @param device
	 * @param action
	 * @throws DeviceException
	 */
	private static void handleUpDownDevice(UpDownDevice device, Object action) throws DeviceException {
		// Up
		if (Boolean.TRUE.equals(action))
			device.up();
		// Down
		else if (Boolean.FALSE.equals(action))
			device.down();
		// Stop
		else
			device.stop();
	}

	/**
	 * Handle Scene Device
	 * @param device
	 * @param action
	 * @throws DeviceException
	 */
	private static void handleSceneDevice(SceneDevice device, Object action) throws DeviceException {
		device.execute();
	}

	/**
	 * Handle Dimmable Device
	 * @param device
	 * @param action
	 * @throws DeviceException
	 */
	private static void handleDimmableDevice(DimmableDevice device,Object action) throws DeviceException {
		// On
		if (Boolean.TRUE.equals(action))
			device.on();
		// Off
		else if (Boolean.FALSE.equals(action))
			device.off();
		// Level
		else if (action instanceof Integer)
			device.dim((Integer)action);
		// Unhandled
		else
			throw new IllegalArgumentException(device + " can't handle action " + action);
	}

	/**
	 * Handle On/Off Device
	 * @param device
	 * @param action
	 * @throws DeviceException
	 */
	private static void handleOnOffDevice(OnOffDevice device, Object action) throws DeviceException {
		// On
		if (Boolean.TRUE.equals(action))
			device.on();
		// Off
		else if (Boolean.FALSE.equals(action))
			device.off();
		// Unhandled
		else
			throw new IllegalArgumentException(device + " can't handle action " + action);
	}
}
