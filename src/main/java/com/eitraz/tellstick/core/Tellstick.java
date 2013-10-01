package com.eitraz.tellstick.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.eitraz.tellstick.core.device.DeviceHandler;
import com.eitraz.tellstick.core.rawdevice.RawDeviceHandler;
import com.eitraz.tellstick.core.sensor.SensorHandler;
import com.sun.jna.Native;

public class Tellstick {
	private static final Logger logger = Logger.getLogger(Tellstick.class);

	private static final int SUPPORTED_METHODS =
			TellstickCoreLibrary.TELLSTICK_BELL |
			TellstickCoreLibrary.TELLSTICK_TURNOFF |
			TellstickCoreLibrary.TELLSTICK_TURNON |
			TellstickCoreLibrary.TELLSTICK_DIM |
			TellstickCoreLibrary.TELLSTICK_LEARN |
			TellstickCoreLibrary.TELLSTICK_EXECUTE |
			TellstickCoreLibrary.TELLSTICK_STOP;

	private static final int SUPPORTED_SENSOR_DATA_TYPES =
			TellstickCoreLibrary.TELLSTICK_TEMPERATURE |
			TellstickCoreLibrary.TELLSTICK_HUMIDITY;


	private TellstickCoreLibrary library;

	private DeviceHandler deviceHandler;
	private RawDeviceHandler rawDeviceHandler;
	private SensorHandler sensorHandler;
	//	private ControllerHandler controllerHandler;

	public Tellstick() {
	}

	/**
	 * Start
	 */
	public void start() {
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");

		// Select Native Library
		String nativeLibrary = selectNativeLibrary(osName, osArch);

		// Load library
		logger.debug("Loading library");
		library = (TellstickCoreLibrary)Native.loadLibrary(nativeLibrary, TellstickCoreLibrary.class);
		library.tdInit();

		// Create Device Handler
		deviceHandler = new DeviceHandler(library, SUPPORTED_METHODS);
		deviceHandler.start();

		// Create Raw Device Handler
		rawDeviceHandler = new RawDeviceHandler(library);
		rawDeviceHandler.start();

		// Sensor Handler
		sensorHandler = new SensorHandler(library, SUPPORTED_SENSOR_DATA_TYPES);
		sensorHandler.start();

		// Controller Handler
		//		controllerHandler = new ControllerHandler(library);
		//		controllerHandler.start();
	}

	/**
	 * Stop
	 */
	public void stop() {
		// Stop Controller Handler
		// if (controllerHandler != null)
		//		controllerHandler.stop();

		// Stop Sensor handler
		if (sensorHandler != null)
			sensorHandler.stop();

		// Stop Raw Device Handler
		if (rawDeviceHandler != null)
			rawDeviceHandler.stop();

		// Stop Device Handler
		if (deviceHandler != null)
			deviceHandler.stop();

		// Stop
		library.tdClose();
	}

	/**
	 * Select Native Library
	 * @param osName
	 * @param osArch
	 * @return native library
	 */
	private String selectNativeLibrary(String osName, String osArch) {
		// Linux
		if (StringUtils.startsWithIgnoreCase(osName, "linux")) {
			logger.debug("Linux library selected");
			return "libtelldus-core.so.2";
		}
		// Windows
		else if (StringUtils.startsWithIgnoreCase(osName, "windows")) {
			logger.debug("Windows library selected");
			return "TelldusCore";
		}
		// Mac
		else if (StringUtils.startsWithIgnoreCase(osName, "mac")) {
			logger.debug("Mac library selected");
			return "TelldusCore.framework";
		}
		// Unsupported
		else {
			throw new RuntimeException(String.format("Unsupported environment: %s, %s", osName, osArch));
		}
	}

	/**
	 * Get Device handler
	 * @return
	 */
	public DeviceHandler getDeviceHandler() {
		return deviceHandler;
	}

	/**
	 * @return the rawDeviceHandler
	 */
	public RawDeviceHandler getRawDeviceHandler() {
		return rawDeviceHandler;
	}

	/**
	 * @param rawDeviceHandler the rawDeviceHandler to set
	 */
	public void setRawDeviceHandler(RawDeviceHandler rawDeviceHandler) {
		this.rawDeviceHandler = rawDeviceHandler;
	}

	/**
	 * @return the sensorHandler
	 */
	public SensorHandler getSensorHandler() {
		return sensorHandler;
	}

	/**
	 * @param sensorHandler the sensorHandler to set
	 */
	public void setSensorHandler(SensorHandler sensorHandler) {
		this.sensorHandler = sensorHandler;
	}

}
