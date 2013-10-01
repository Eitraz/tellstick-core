package com.eitraz.tellstick.core.sensor;

public class Sensor {
	//	protocol: fineoffset, model: temperature, id: 199, dataType: 1, value: 11.3, timestamp: 1380376371, callbackId: 4

	private final String protocol;
	private final String model;
	private final int id;
	private final int dataType;
	private final String value;
	private final long timestamp;

	public Sensor(int id, String protocol, String model, int dataType, String value, long timestamp) {
		this.protocol = protocol;
		this.model = model;
		this.id = id;
		this.dataType = dataType;
		this.value = value;
		this.timestamp = timestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sensor [protocol=" + protocol + ", model=" + model + ", id="
				+ id + ", dataType=" + dataType + ", value=" + value + "]";
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the dataType
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Sensor other = (Sensor) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
