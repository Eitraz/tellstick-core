package com.eitraz.tellstick.core.device;

import com.eitraz.tellstick.core.TellstickException;


public class DeviceException extends TellstickException {
	private static final long serialVersionUID = 4387153250064695364L;

	public DeviceException(Device device, int errorNo) {
		super(device.getName(), device.getLibrary(), errorNo);
	}
}
