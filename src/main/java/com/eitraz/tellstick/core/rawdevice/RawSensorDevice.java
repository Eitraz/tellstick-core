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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RawSensorDevice other = (RawSensorDevice) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;

		return super.equals(obj);
	}


}
