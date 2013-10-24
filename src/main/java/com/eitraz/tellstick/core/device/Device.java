package com.eitraz.tellstick.core.device;

import org.apache.log4j.Logger;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.sun.jna.Pointer;

public abstract class Device implements Comparable<Device> {
	protected static final Logger logger = Logger.getLogger(Device.class);

	private final DeviceHandler deviceHandler;
	private final TellstickCoreLibrary library;

	private final int deviceId;

	private final String name;
	private final String model;
	private final String protocol;
	private final int deviceType;

	public Device(DeviceHandler deviceHandler, int deviceId) {
		this.deviceHandler = deviceHandler;
		this.deviceId = deviceId;

		library = deviceHandler.getLibrary();

		// Name
		Pointer namePointer = library.tdGetName(deviceId);
		name = namePointer.getString(0);
		library.tdReleaseString(namePointer);

		// Model
		Pointer modelPointer = library.tdGetModel(deviceId);
		model = modelPointer.getString(0);
		library.tdReleaseString(modelPointer);

		// Protocol
		Pointer protocolPointer = library.tdGetProtocol(deviceId);
		protocol = protocolPointer.getString(0);
		library.tdReleaseString(protocolPointer);

		deviceType = library.tdGetDeviceType(deviceId);
	}

	/**
	 * @return the deviceId
	 */
	public int getDeviceId() {
		return deviceId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @return the deviceType
	 */
	public int getDeviceType() {
		return deviceType;
	}

	/**
	 * Get Status
	 * @return status
	 */
	public int getStatus() {
		return library.tdLastSentCommand(deviceId, deviceHandler.getSupportedMethods());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Device device) {
		return getDeviceId() - device.getDeviceId();
	}

	/**
	 * Get Library
	 * @return library
	 */
	protected TellstickCoreLibrary getLibrary() {
		return library;
	}

	/**
	 * Get Device Handler
	 * @return device handler
	 */
	protected DeviceHandler getDeviceHandler() {
		return deviceHandler;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Device [deviceId=" + deviceId + ", name=" + name + ", model="
				+ model + ", protocol=" + protocol + ", deviceType="
				+ deviceType + ", getClass()=" + getClass().getSimpleName() + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deviceId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (deviceId != other.deviceId)
			return false;
		return true;
	}

}
