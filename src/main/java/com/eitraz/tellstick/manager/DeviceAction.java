package com.eitraz.tellstick.manager;

import com.eitraz.tellstick.core.device.Device;

/**
 * Device Action
 * 
 * @author Petter Alstermark
 *
 */
public class DeviceAction implements Comparable<DeviceAction> {
	private final Device device;
	private Object action;
	private int counter;
	private long time;

	/**
	 * @param device
	 * @param action
	 */
	public DeviceAction(Device device, Object action, int counter) {
		this.device = device;
		this.action = action;
		this.counter = counter;

		time = System.currentTimeMillis();
	}

	/**
	 * @return the action
	 */
	public Object getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Object action) {
		this.action = action;
	}

	/**
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * @return the device
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public int compareTo(DeviceAction deviceAction) {
		return new Long(getTime() - deviceAction.getTime()).intValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((device == null) ? 0 : device.hashCode());
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
		DeviceAction other = (DeviceAction) obj;
		if (device == null) {
			if (other.device != null)
				return false;
		} else if (!device.equals(other.device))
			return false;
		return true;
	}
}
