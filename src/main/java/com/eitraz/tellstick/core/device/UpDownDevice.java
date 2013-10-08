package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

public class UpDownDevice extends Device {

	public UpDownDevice(DeviceHandler deviceHandler, int deviceId) {
		super(deviceHandler, deviceId);
	}

	/**
	 * Up
	 * @throws DeviceException
	 */
	public void up() throws DeviceException {
		logger.debug("UP " + toString());

		int status = getLibrary().tdUp(getDeviceId());

		if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
			throw new DeviceException(this, status);
	}

	/**
	 * Down
	 * @throws DeviceException
	 */
	public void down() throws DeviceException {
		logger.debug("DOWN " + toString());

		int status = getLibrary().tdDown(getDeviceId());

		if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
			throw new DeviceException(this, status);
	}

	/**
	 * Stop
	 * @throws DeviceException
	 */
	public void stop() throws DeviceException {
		logger.debug("STOP " + toString());

		int status = getLibrary().tdStop(getDeviceId());

		if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
			throw new DeviceException(this, status);
	}

}
