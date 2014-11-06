package com.eitraz.tellstick.core.rawdevice;

import java.util.Map;

public class RawSensorDevice extends RawDevice {
    private final String id;

    /**
     * @param _class     class
     * @param protocol   protocol
     * @param model      model
     * @param id         id
     * @param parameters parameters
     */
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

    @Override
    public String toString() {
        return "RawSensorDevice [id=" + id + ", toString()=" + super.toString()
                + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RawSensorDevice that = (RawSensorDevice) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
