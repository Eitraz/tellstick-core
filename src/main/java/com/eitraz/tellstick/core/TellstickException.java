package com.eitraz.tellstick.core;

import com.sun.jna.Pointer;

public class TellstickException extends Exception {
	private static final long serialVersionUID = -3546899773397409514L;

	public TellstickException(TellstickCoreLibrary library, int errorNo) {
		super(getErrorString(library, errorNo));
	}

	public TellstickException(String message, TellstickCoreLibrary library, int errorNo) {
		super(message + ", error: " + getErrorString(library, errorNo));
	}

	/**
	 * Get Error String
	 * @param library
	 * @param errorNo
	 * @return
	 */
	protected static String getErrorString(TellstickCoreLibrary library, int errorNo) {
		Pointer errorStringPointer =  library.tdGetErrorString(errorNo);
		String errorString = errorStringPointer.getString(0);
		library.tdReleaseString(errorStringPointer);

		return errorString;
	}

}
