package com.eitraz.tellstick.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.eitraz.tellstick.core.device.Device;
import com.eitraz.tellstick.core.device.DeviceHandler;
import com.eitraz.tellstick.manager.TellstickDeviceManager;

@Ignore
public class TellstickTestCase {
	@Test
	public void testGetDevices() {
		Tellstick tellstick = new Tellstick();
		try {
			tellstick.start();

			DeviceHandler deviceHandler = tellstick.getDeviceHandler();
			List<Device> devices = deviceHandler.getDevices();

			for (Device device : devices) {
				System.out.println(device);
			}
		} finally {
			tellstick.stop();
		}
	}

	public static void main(String[] args) {
		Tellstick tellstick = new Tellstick();
		try {
			tellstick.start();

			TellstickDeviceManager manager = new TellstickDeviceManager(tellstick.getDeviceHandler());

			Map<Integer, Device> devices = manager.getDevices();
			for (Device device : devices.values()) {
				System.out.println(device);
			}

			try {
				System.in.read();
			} catch (IOException e) {
			}

		} finally {
			tellstick.stop();
		}
	}
}
