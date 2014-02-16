package com.eitraz.tellstick.core;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

/**
 * Generated: Sat Sep 28 16:21:16 CEST 2013
 */
public interface CopyOfTellstickCoreLibrary extends Library {

	/**
	 * Defines
	 */

	// #define TELLSTICK_TURNON	1
	public static final int TELLSTICK_TURNON = 1;

	// #define TELLSTICK_TURNOFF	2
	public static final int TELLSTICK_TURNOFF = 2;

	// #define TELLSTICK_BELL		4
	public static final int TELLSTICK_BELL = 4;

	// #define TELLSTICK_TOGGLE	8
	public static final int TELLSTICK_TOGGLE = 8;

	// #define TELLSTICK_DIM		16
	public static final int TELLSTICK_DIM = 16;

	// #define TELLSTICK_LEARN		32
	public static final int TELLSTICK_LEARN = 32;

	// #define TELLSTICK_EXECUTE	64
	public static final int TELLSTICK_EXECUTE = 64;

	// #define TELLSTICK_UP		128
	public static final int TELLSTICK_UP = 128;

	// #define TELLSTICK_DOWN		256
	public static final int TELLSTICK_DOWN = 256;

	// #define TELLSTICK_STOP		512
	public static final int TELLSTICK_STOP = 512;

	// #define TELLSTICK_TEMPERATURE	1
	public static final int TELLSTICK_TEMPERATURE = 1;

	// #define TELLSTICK_HUMIDITY		2
	public static final int TELLSTICK_HUMIDITY = 2;

	// #define TELLSTICK_RAINRATE		4
	public static final int TELLSTICK_RAINRATE = 4;

	// #define TELLSTICK_RAINTOTAL		8
	public static final int TELLSTICK_RAINTOTAL = 8;

	// #define TELLSTICK_WINDDIRECTION	16
	public static final int TELLSTICK_WINDDIRECTION = 16;

	// #define TELLSTICK_WINDAVERAGE	32
	public static final int TELLSTICK_WINDAVERAGE = 32;

	// #define TELLSTICK_WINDGUST		64
	public static final int TELLSTICK_WINDGUST = 64;

	// #define TELLSTICK_SUCCESS 0
	public static final int TELLSTICK_SUCCESS = 0;

	// #define TELLSTICK_ERROR_NOT_FOUND -1
	public static final int TELLSTICK_ERROR_NOT_FOUND = -1;

	// #define TELLSTICK_ERROR_PERMISSION_DENIED -2
	public static final int TELLSTICK_ERROR_PERMISSION_DENIED = -2;

	// #define TELLSTICK_ERROR_DEVICE_NOT_FOUND -3
	public static final int TELLSTICK_ERROR_DEVICE_NOT_FOUND = -3;

	// #define TELLSTICK_ERROR_METHOD_NOT_SUPPORTED -4
	public static final int TELLSTICK_ERROR_METHOD_NOT_SUPPORTED = -4;

	// #define TELLSTICK_ERROR_COMMUNICATION -5
	public static final int TELLSTICK_ERROR_COMMUNICATION = -5;

	// #define TELLSTICK_ERROR_CONNECTING_SERVICE -6
	public static final int TELLSTICK_ERROR_CONNECTING_SERVICE = -6;

	// #define TELLSTICK_ERROR_UNKNOWN_RESPONSE -7
	public static final int TELLSTICK_ERROR_UNKNOWN_RESPONSE = -7;

	// #define TELLSTICK_ERROR_SYNTAX -8
	public static final int TELLSTICK_ERROR_SYNTAX = -8;

	// #define TELLSTICK_ERROR_BROKEN_PIPE -9
	public static final int TELLSTICK_ERROR_BROKEN_PIPE = -9;

	// #define TELLSTICK_ERROR_COMMUNICATING_SERVICE -10
	public static final int TELLSTICK_ERROR_COMMUNICATING_SERVICE = -10;

	// #define TELLSTICK_ERROR_CONFIG_SYNTAX -11
	public static final int TELLSTICK_ERROR_CONFIG_SYNTAX = -11;

	// #define TELLSTICK_ERROR_UNKNOWN -99
	public static final int TELLSTICK_ERROR_UNKNOWN = -99;

	// #define TELLSTICK_TYPE_DEVICE	1
	public static final int TELLSTICK_TYPE_DEVICE = 1;

	// #define TELLSTICK_TYPE_GROUP	2
	public static final int TELLSTICK_TYPE_GROUP = 2;

	// #define TELLSTICK_TYPE_SCENE	3
	public static final int TELLSTICK_TYPE_SCENE = 3;

	// #define TELLSTICK_CONTROLLER_TELLSTICK      1
	public static final int TELLSTICK_CONTROLLER_TELLSTICK = 1;

	// #define TELLSTICK_CONTROLLER_TELLSTICK_DUO  2
	public static final int TELLSTICK_CONTROLLER_TELLSTICK_DUO = 2;

	// #define TELLSTICK_CONTROLLER_TELLSTICK_NET  3
	public static final int TELLSTICK_CONTROLLER_TELLSTICK_NET = 3;

	// #define TELLSTICK_DEVICE_ADDED			1
	public static final int TELLSTICK_DEVICE_ADDED = 1;

	// #define TELLSTICK_DEVICE_CHANGED		2
	public static final int TELLSTICK_DEVICE_CHANGED = 2;

	// #define TELLSTICK_DEVICE_REMOVED		3
	public static final int TELLSTICK_DEVICE_REMOVED = 3;

	// #define TELLSTICK_DEVICE_STATE_CHANGED	4
	public static final int TELLSTICK_DEVICE_STATE_CHANGED = 4;

	// #define TELLSTICK_CHANGE_NAME			1
	public static final int TELLSTICK_CHANGE_NAME = 1;

	// #define TELLSTICK_CHANGE_PROTOCOL		2
	public static final int TELLSTICK_CHANGE_PROTOCOL = 2;

	// #define TELLSTICK_CHANGE_MODEL			3
	public static final int TELLSTICK_CHANGE_MODEL = 3;

	// #define TELLSTICK_CHANGE_METHOD			4
	public static final int TELLSTICK_CHANGE_METHOD = 4;

	// #define TELLSTICK_CHANGE_AVAILABLE		5
	public static final int TELLSTICK_CHANGE_AVAILABLE = 5;

	// #define TELLSTICK_CHANGE_FIRMWARE		6
	public static final int TELLSTICK_CHANGE_FIRMWARE = 6;


	/**
	 * Events
	 */

	// typedef void (WINAPI *TDDeviceEvent)(int deviceId, int method, const char *data, int callbackId, void *context);
	public interface TDDeviceEvent extends Callback {
		public void event(int deviceId, int method, String data, int callbackId, Pointer context);
	}

	// typedef void (WINAPI *TDDeviceChangeEvent)(int deviceId, int changeEvent, int changeType, int callbackId, void *context);
	public interface TDDeviceChangeEvent extends Callback {
		public void event(int deviceId, int changeEvent, int changeType, int callbackId, Pointer context);
	}

	// typedef void (WINAPI *TDRawDeviceEvent)(const char *data, int controllerId, int callbackId, void *context);
	public interface TDRawDeviceEvent extends Callback {
		public void event(String data, int controllerId, int callbackId, Pointer context);
	}

	// typedef void (WINAPI *TDSensorEvent)(const char *protocol, const char *model, int id, int dataType, const char *value, int timestamp, int callbackId, void *context);
	public interface TDSensorEvent extends Callback {
		public void event(String protocol, String model, int id, int dataType, String value, int timestamp, int callbackId, Pointer context);
	}

	// typedef void (WINAPI *TDControllerEvent)(int controllerId, int changeEvent, int changeType, const char *newValue, int callbackId, void *context);
	public interface TDControllerEvent extends Callback {
		public void event(int controllerId, int changeEvent, int changeType, String newValue, int callbackId, Pointer context);
	}


	/**
	 * Methods
	 */

	// TELLSTICK_API void WINAPI tdInit(void);
	public void tdInit();

	// TELLSTICK_API int WINAPI tdRegisterDeviceEvent( TDDeviceEvent eventFunction, void *context );
	public int tdRegisterDeviceEvent(TDDeviceEvent eventFunction, Pointer context);

	// TELLSTICK_API int WINAPI tdRegisterDeviceChangeEvent( TDDeviceChangeEvent eventFunction, void *context);
	public int tdRegisterDeviceChangeEvent(TDDeviceChangeEvent eventFunction, Pointer context);

	// TELLSTICK_API int WINAPI tdRegisterRawDeviceEvent( TDRawDeviceEvent eventFunction, void *context );
	public int tdRegisterRawDeviceEvent(TDRawDeviceEvent eventFunction, Pointer context);

	// TELLSTICK_API int WINAPI tdRegisterSensorEvent( TDSensorEvent eventFunction, void *context );
	public int tdRegisterSensorEvent(TDSensorEvent eventFunction, Pointer context);

	// TELLSTICK_API int WINAPI tdRegisterControllerEvent( TDControllerEvent eventFunction, void *context);
	public int tdRegisterControllerEvent(TDControllerEvent eventFunction, Pointer context);

	// TELLSTICK_API int WINAPI tdUnregisterCallback( int callbackId );
	public int tdUnregisterCallback(int callbackId);

	// TELLSTICK_API void WINAPI tdClose(void);
	public void tdClose();

	// TELLSTICK_API void WINAPI tdReleaseString(char *thestring);
	public void tdReleaseString(Pointer thestring);

	// TELLSTICK_API int WINAPI tdTurnOn(int intDeviceId);
	public int tdTurnOn(int intDeviceId);

	// TELLSTICK_API int WINAPI tdTurnOff(int intDeviceId);
	public int tdTurnOff(int intDeviceId);

	// TELLSTICK_API int WINAPI tdBell(int intDeviceId);
	public int tdBell(int intDeviceId);

	// TELLSTICK_API int WINAPI tdDim(int intDeviceId, unsigned char level);
	public int tdDim(int intDeviceId, int level);

	// TELLSTICK_API int WINAPI tdExecute(int intDeviceId);
	public int tdExecute(int intDeviceId);

	// TELLSTICK_API int WINAPI tdUp(int intDeviceId);
	public int tdUp(int intDeviceId);

	// TELLSTICK_API int WINAPI tdDown(int intDeviceId);
	public int tdDown(int intDeviceId);

	// TELLSTICK_API int WINAPI tdStop(int intDeviceId);
	public int tdStop(int intDeviceId);

	// TELLSTICK_API int WINAPI tdLearn(int intDeviceId);
	public int tdLearn(int intDeviceId);

	// TELLSTICK_API int WINAPI tdMethods(int id, int methodsSupported);
	public int tdMethods(int id, int methodsSupported);

	// TELLSTICK_API int WINAPI tdLastSentCommand( int intDeviceId, int methodsSupported );
	public int tdLastSentCommand(int intDeviceId, int methodsSupported);

	// TELLSTICK_API int WINAPI tdGetNumberOfDevices();
	public int tdGetNumberOfDevices();

	// TELLSTICK_API int WINAPI tdGetDeviceId(int intDeviceIndex);
	public int tdGetDeviceId(int intDeviceIndex);

	// TELLSTICK_API int WINAPI tdGetDeviceType(int intDeviceId);
	public int tdGetDeviceType(int intDeviceId);

	// TELLSTICK_API char * WINAPI tdGetErrorString(int intErrorNo);
	public Pointer tdGetErrorString(int intErrorNo);

	// TELLSTICK_API char * WINAPI tdGetName(int intDeviceId);
	public Pointer tdGetName(int intDeviceId);

	// TELLSTICK_API bool WINAPI tdSetName(int intDeviceId, const char* chNewName);
	public boolean tdSetName(int intDeviceId, String strNewName);

	// TELLSTICK_API char * WINAPI tdGetProtocol(int intDeviceId);
	public Pointer tdGetProtocol(int intDeviceId);

	// TELLSTICK_API bool WINAPI tdSetProtocol(int intDeviceId, const char* strProtocol);
	public boolean tdSetProtocol(int intDeviceId, String strProtocol);

	// TELLSTICK_API char * WINAPI tdGetModel(int intDeviceId);
	public Pointer tdGetModel(int intDeviceId);

	// TELLSTICK_API bool WINAPI tdSetModel(int intDeviceId, const char *intModel);
	public boolean tdSetModel(int intDeviceId, String strModel);

	// TELLSTICK_API char * WINAPI tdGetDeviceParameter(int intDeviceId, const char *strName, const char *defaultValue);
	public Pointer tdGetDeviceParameter(int intDeviceId, String strName, String defaultValue);

	// TELLSTICK_API bool WINAPI tdSetDeviceParameter(int intDeviceId, const char *strName, const char* strValue);
	public boolean tdSetDeviceParameter(int intDeviceId, String strName, String strValue);

	// TELLSTICK_API int WINAPI tdAddDevice();
	public int tdAddDevice();

	// TELLSTICK_API bool WINAPI tdRemoveDevice(int intDeviceId);
	public boolean tdRemoveDevice(int intDeviceId);

	// TELLSTICK_API int WINAPI tdSendRawCommand(const char *command, int reserved);
	public int tdSendRawCommand(String command, int reserved);

	// TELLSTICK_API void WINAPI tdConnectTellStickController(int vid, int pid, const char *serial);
	public void tdConnectTellStickController(int vid, int pid, String serial);

	// TELLSTICK_API void WINAPI tdDisconnectTellStickController(int vid, int pid, const char *serial);
	public void tdDisconnectTellStickController(int vid, int pid, String serial);

	// TELLSTICK_API int WINAPI tdSensor(char *protocol, int protocolLen, char *model, int modelLen, int *id, int *dataTypes);
	public int tdSensor(Pointer protocol, int protocolLen, Pointer model, int modelLen, IntByReference id, IntByReference dataTypes);

	// TELLSTICK_API int WINAPI tdSensorValue(const char *protocol, const char *model, int id, int dataType, char *value, int len, int *timestamp);
	public int tdSensorValue(String protocol, String model, int id, int dataType, Pointer value, int len, LongByReference timestamp);

	// TELLSTICK_API int WINAPI tdController(int *controllerId, int *controllerType, char *name, int nameLen, int *available);
	public int tdController(boolean controllerId, boolean controllerType, Pointer name, int nameLen, boolean available);

	// TELLSTICK_API int WINAPI tdControllerValue(int controllerId, const char *name, char *value, int valueLen);
	public int tdControllerValue(int controllerId, String name, Pointer value, int valueLen);

	// TELLSTICK_API int WINAPI tdSetControllerValue(int controllerId, const char *name, const char *value);
	public int tdSetControllerValue(int controllerId, String name, String value);

	// TELLSTICK_API int WINAPI tdRemoveController(int controllerId);
	public int tdRemoveController(int controllerId);

}
