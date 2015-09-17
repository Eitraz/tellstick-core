package com.eitraz.tellstick.core;

import com.sun.jna.Pointer;

/**
 * Tellstick Exception
 */
public class TellstickException extends Exception {
    public TellstickException(TellstickCoreLibrary library, int errorNo) {
        super(getErrorString(library, errorNo));
    }

    public TellstickException(String message, TellstickCoreLibrary library, int errorNo) {
        super(message + ", error: " + getErrorString(library, errorNo));
    }

    /**
     * Get Error String
     *
     * @param library tellstick core library
     * @param errorNo error no
     * @return error string
     */
    private static String getErrorString(TellstickCoreLibrary library, int errorNo) {
        Pointer errorStringPointer = library.tdGetErrorString(errorNo);
        String errorString = errorStringPointer.getString(0);
        library.tdReleaseString(errorStringPointer);

        return errorString;
    }

}
