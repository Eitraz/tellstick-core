package com.eitraz.tellstick.core.rawdevice;

import java.util.Collections;
import java.util.Map;

/**
 * @author Petter
 *
 */
public class RawDevice {
	private final String _class;
	private final String protocol;
	private final String model;
	private final Map<String, String> parameters;

	public RawDevice(String _class, String protocol, String model, Map<String, String> parameters) {
		this._class = _class;
		this.protocol = protocol;
		this.model = model;
		this.parameters = parameters;
	}

	/**
	 * @return the _class
	 */
	public String get_class() {
		return _class;
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
	 * @return the parameters
	 */
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RawDevice [_class=" + _class + ", protocol=" + protocol
				+ ", model=" + model + ", parameters=" + parameters + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_class == null) ? 0 : _class.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result
				+ ((protocol == null) ? 0 : protocol.hashCode());
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
		RawDevice other = (RawDevice) obj;
		if (_class == null) {
			if (other._class != null)
				return false;
		} else if (!_class.equals(other._class))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		return true;
	}

}
