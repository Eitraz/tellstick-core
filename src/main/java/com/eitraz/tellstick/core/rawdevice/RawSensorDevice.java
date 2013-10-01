package com.eitraz.tellstick.core.rawdevice;

import java.util.Map;

public class RawSensorDevice extends RawDevice {
	private final String id;

	public RawSensorDevice(String _class, String protocol, String model, String id, Map<String, String> parameters) {
		super(_class, protocol, model, parameters);

		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RawSensorDevice [id=" + id + ", toString()=" + super.toString()
				+ "]";
	}

}
