package com.eitraz.tellstick.core.device;

public class DeviceNotSupportedException extends Exception {
	private static final long serialVersionUID = 1L;

	public DeviceNotSupportedException(String message) {
		super(message);
	}
}
