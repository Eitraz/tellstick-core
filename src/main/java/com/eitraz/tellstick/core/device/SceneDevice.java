package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickCoreLibrary;


public class SceneDevice extends Device {

	public SceneDevice(DeviceHandler deviceHandler, int deviceId) {
		super(deviceHandler, deviceId);
	}

	/**
	 * Execute
	 * @throws DeviceException
	 */
	public void execute() throws DeviceException {
		logger.debug("EXECUTE " + toString());

		int status = getLibrary().tdExecute(getDeviceId());

		if (status != TellstickCoreLibrary.TELLSTICK_SUCCESS)
			throw new DeviceException(this, status);
	}

}
