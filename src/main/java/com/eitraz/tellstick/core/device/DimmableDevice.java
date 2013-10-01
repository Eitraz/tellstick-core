package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

public class DimmableDevice extends Device {

	public DimmableDevice(DeviceHandler deviceHandler, int deviceId) {
		super(deviceHandler, deviceId);
	}

	public void dim(int level) throws DeviceException {
		logger.debug("DIM " + level + " " + toString());

		if (level < 0 || level > 255)
			throw new IllegalArgumentException("Dim level must be between 0 and 255.");

		int status = getLibrary().tdDim(getDeviceId(), level);
		if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
			throw new DeviceException(this, status);
	}

	/**
	 * On (max level)
	 * @throws DeviceException
	 */
	public void on() throws DeviceException {
		dim(255);
	}

	/**
	 * Off (min level)
	 * @throws DeviceException
	 */
	public void off() throws DeviceException {
		dim(0);
	}

	public boolean isOn() {
		boolean isOn = (getStatus() & TellstickCoreLibrary.TELLSTICK_TURNON) > 0;
		return isOn || ((TellstickCoreLibrary.TELLSTICK_DIM & getStatus()) > 0);
	}

	@Override
	public String getType() {
		return "Dimmable Device";
	}
}
