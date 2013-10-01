package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

public class OnOffDevice extends Device {

	public OnOffDevice(DeviceHandler deviceHandler, int deviceId) {
		super(deviceHandler, deviceId);
	}

	/**
	 * Turn on
	 * @throws DeviceException
	 */
	public void on() throws DeviceException {
		logger.debug("ON " + toString());

		int status = getLibrary().tdTurnOn(getDeviceId());

		if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
			throw new DeviceException(this, status);
	}

	/**
	 * Turn Off
	 * @throws DeviceException
	 */
	public void off() throws DeviceException {
		logger.debug("OFF " + toString());

		int status = getLibrary().tdTurnOff(getDeviceId());

		if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
			throw new DeviceException(this, status);
	}

	/**
	 * Is On
	 * @return
	 */
	public boolean isOn() {
		return (getStatus() & TellstickCoreLibrary.TELLSTICK_TURNON) > 0;
	}

	@Override
	public String getType() {
		return "On/Off Device";
	}
}
