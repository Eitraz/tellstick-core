package com.eitraz.tellstick.core.device;



public interface DeviceEventListener {
	/**
	 * Device Changed
	 * @param deviceId
	 * @param device
	 */
	public void deviceChanged(int deviceId, Device device);

	/**
	 * Device Added
	 * @param deviceId
	 * @param device
	 */
	public void deviceAdded(int deviceId, Device device);

	/**
	 * Device Removed
	 * @param deviceId
	 */
	public void deviceRemoved(int deviceId);
}
