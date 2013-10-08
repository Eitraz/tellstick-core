package com.eitraz.tellstick;

import java.io.IOException;

import com.eitraz.tellstick.core.Tellstick;
import com.eitraz.tellstick.core.device.Device;
import com.eitraz.tellstick.core.device.DeviceEventListener;
import com.eitraz.tellstick.core.device.DeviceHandler;
import com.eitraz.tellstick.core.rawdevice.RawDevice;
import com.eitraz.tellstick.core.rawdevice.RawDeviceEventListener;
import com.eitraz.tellstick.core.sensor.Sensor;
import com.eitraz.tellstick.core.sensor.SensorEventListener;
import com.eitraz.tellstick.core.sensor.SensorHandler;

public class Test {
	public static void main(String[] args) {

		Tellstick tellstick = new Tellstick();
		try {
			tellstick.start();

			//			try {
			//				Map<String, String> parameters = new HashMap<String, String>();
			//				parameters.put("house", "7791006");
			//				parameters.put("unit", "2");
			//				parameters.put("group", "0");
			//				tellstick.getDeviceHandler().createDevice("Test remote 1", "selflearning-switch", "arctech", parameters);
			//			} catch (TellstickException | DeviceNotSupportedException e1) {
			//				e1.printStackTrace();
			//			}

			System.out.println("===== Devices =======================================================================");
			DeviceHandler deviceHandler = tellstick.getDeviceHandler();
			for (Device device : deviceHandler.getDevices()) {
				System.out.println(device);
			}
			// Device Event Listener
			deviceHandler.addDeviceEventListener(new DeviceEventListener() {
				public void deviceRemoved(int deviceId) {
					System.out.println("Device #" + deviceId + " removed");
				}

				public void deviceChanged(int deviceId, Device device) {
					System.out.println("Device #" + deviceId + " changed: " + device);
				}

				public void deviceAdded(int deviceId, Device device) {
					System.out.println("Device #" + deviceId + " added: " + device);
				}
			});

			System.out.println("===== Sensors =======================================================================");
			// Sensors
			SensorHandler sensorHandler = tellstick.getSensorHandler();
			for (Sensor sensor : sensorHandler.getSensors()) {
				System.out.println(sensor);
			}
			// Sensor Event Listener
			sensorHandler.addDeviceEventListener(new SensorEventListener() {
				public void sensorEvent(Sensor sensor) {
					System.out.println("Sensor event: " + sensor);
				}
			});
			System.out.println("=====================================================================================");

			// Raw Device Event Listener
			tellstick.getRawDeviceHandler().addDeviceEventListener(new RawDeviceEventListener() {
				public void rawDeviceEvent(RawDevice device) {
					System.out.println("\tRaw device event: " + device);
				}
			});

			try {
				System.in.read();
			} catch (IOException e) {
			}

		} finally {
			tellstick.stop();
		}
	}
}
