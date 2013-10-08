package com.eitraz.tellstick.core.rawdevice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.eitraz.tellstick.core.TellstickCoreLibrary;
import com.eitraz.tellstick.core.TellstickCoreLibrary.TDRawDeviceEvent;
import com.eitraz.tellstick.core.util.TimeoutHandler;
import com.sun.jna.Pointer;

public class RawDeviceHandler {
	private static final Logger logger = Logger.getLogger(RawDeviceHandler.class);

	private static final int TIMEOUT = 5000;

	private static final String DELIMITER_MAJOR = ";";
	private static final String DELIMITER_MINOR = ":";

	public static final String _CLASS = "class";
	public static final String PROTOCOL = "protocol";
	public static final String MODEL = "model";

	public static final String COMMAND = "command";
	public static final String SENSOR = "sensor";

	public static final String ID = "id";
	public static final String METHOD = "method";

	public static final String HOUSE = "house";
	public static final String UNIT = "unit";
	public static final String GROUP = "group";

	public static final String BELL = "bell";
	public static final String ON = "turnon";
	public static final String OFF = "turnoff";

	private final Set<RawDeviceEventListener> rawDeviceEventListeners = new HashSet<RawDeviceEventListener>();

	private final TellstickCoreLibrary library;

	private int rawDeviceEventCallbackId = -1;
	private TDRawDeviceEventListener rawDeviceEventListener;

	public RawDeviceHandler(TellstickCoreLibrary library) {
		this.library = library;
	}

	/**
	 * Add Raw Device Event Listen
	 * @param listener
	 */
	public void addDeviceEventListener(RawDeviceEventListener listener) {
		rawDeviceEventListeners.add(listener);
	}

	/**
	 * Remove Raw Device Event Listener
	 * @param listener
	 */
	public void removeDeviceEventListener(RawDeviceEventListener listener) {
		rawDeviceEventListeners.remove(listener);
	}

	/**
	 * Start
	 */
	public void start() {
		// Raw Device Event Listener
		logger.debug("Starting Raw Device Event Listener");
		rawDeviceEventListener = new TDRawDeviceEventListener();
		rawDeviceEventCallbackId = library.tdRegisterRawDeviceEvent(rawDeviceEventListener, null);
	}

	/**
	 * Stop
	 */
	public void stop() {
		// Stop Raw Device Event Listener
		if (rawDeviceEventCallbackId != -1) {
			logger.debug("Stopping Raw Device Event Listener");
			library.tdUnregisterCallback(rawDeviceEventCallbackId);
			rawDeviceEventCallbackId = -1;
		}
	}

	/**
	 * Get Raw Device
	 * @param parameters
	 * @return raw device
	 */
	private RawDevice getRawDevice(Map<String, String> parameters) {
		String _class = parameters.remove(_CLASS);
		String protocol = parameters.remove(PROTOCOL);
		String model = parameters.remove(MODEL);

		// Command
		if (COMMAND.equalsIgnoreCase(_class)) {
			String method = parameters.remove(METHOD);
			return new RawCommandDevice(_class, protocol, model, method, parameters);
		}
		// Sensor
		else if (SENSOR.equalsIgnoreCase(_class)) {
			String id = parameters.remove(ID);
			return new RawSensorDevice(_class, protocol, model, id, parameters);
		}
		// Other
		else
			return new RawDevice(_class, protocol, model, parameters);
	}

	/**
	 * Fire Raw Device Event
	 * @param device
	 */
	private void fireRawDeviceEvent(RawDevice device) {
		for (RawDeviceEventListener listener : rawDeviceEventListeners) {
			listener.rawDeviceEvent(device);
		}
	}

	/**
	 * Raw Device Event Listener
	 */
	private class TDRawDeviceEventListener implements TDRawDeviceEvent {
		private final TimeoutHandler<String> timeoutHandler = new TimeoutHandler<String>(TIMEOUT);

		/*
		 * (non-Javadoc)
		 * @see com.eitraz.tellstick.core.TelldusCoreLibrary.TDRawDeviceEvent#event(java.lang.String, int, int, com.sun.jna.Pointer)
		 */
		public void event(Pointer dataPointer, int controllerId, int callbackId, Pointer context) {
			String data = dataPointer.getString(0);

			// Don't fire event to often
			if (!timeoutHandler.isReady(data))
				return;

			// Debug log
			if (logger.isDebugEnabled())
				logger.debug(data);

			String[] split = data.split(DELIMITER_MAJOR);

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("controllerId", Integer.toString(controllerId));
			for (String string : split) {
				String[] pair = string.split(DELIMITER_MINOR);
				if (pair.length == 2)
					parameters.put(pair[0].trim(), pair[1].trim());
			}

			// Fire event
			fireRawDeviceEvent(getRawDevice(parameters));
		}

	}

}
