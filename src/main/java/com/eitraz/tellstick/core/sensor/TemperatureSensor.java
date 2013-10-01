package com.eitraz.tellstick.core.sensor;

import com.eitraz.tellstick.core.TellstickCoreLibrary;

public class TemperatureSensor extends Sensor {

	public TemperatureSensor(int id, String protocol, String model, String value, long timestamp) {
		super(id, protocol, model, TellstickCoreLibrary.TELLSTICK_TEMPERATURE, value, timestamp);
	}

	/**
	 * Get Temperature
	 * @return
	 */
	public double getTemperature() {
		return Double.parseDouble(getValue());
	}
}
