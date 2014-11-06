package com.eitraz.tellstick.core.rawdevice;

import java.util.Collections;
import java.util.Map;

/**
 * @author Petter
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

    @Override
    public String toString() {
        return "RawDevice [_class=" + _class + ", protocol=" + protocol
                + ", model=" + model + ", parameters=" + parameters + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RawDevice rawDevice = (RawDevice) o;

        if (!_class.equals(rawDevice._class)) return false;
        if (!model.equals(rawDevice.model)) return false;
        if (!protocol.equals(rawDevice.protocol)) return false;

        if (parameters.get("house") == null) {
            if (rawDevice.parameters.get("house") != null)
                return false;
        } else if (!parameters.get("house").equals(rawDevice.parameters.get("house")))
            return false;

        if (parameters.get("unit") == null) {
            if (rawDevice.parameters.get("unit") != null)
                return false;
        } else if (!parameters.get("unit").equals(rawDevice.parameters.get("unit")))
            return false;

        if (parameters.get("code") == null) {
            if (rawDevice.parameters.get("code") != null)
                return false;
        } else if (!parameters.get("code").equals(rawDevice.parameters.get("code")))
            return false;

        if (parameters.get("group") == null) {
            if (rawDevice.parameters.get("group") != null)
                return false;
        } else if (!parameters.get("group").equals(rawDevice.parameters.get("group")))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _class.hashCode();
        result = 31 * result + protocol.hashCode();
        result = 31 * result + model.hashCode();

        result = 31 * result + ((parameters.get("house") == null) ? 0 : parameters.get("house").hashCode());
        result = 31 * result + ((parameters.get("unit") == null) ? 0 : parameters.get("unit").hashCode());
        result = 31 * result + ((parameters.get("code") == null) ? 0 : parameters.get("code").hashCode());
        result = 31 * result + ((parameters.get("group") == null) ? 0 : parameters.get("group").hashCode());

        return result;
    }

}
