package com.pliesveld.flashnote.exception;

public class AudioFormatNotSupportedException extends Exception {
    public AudioFormatNotSupportedException() {
    }

    public AudioFormatNotSupportedException(String message) {
        super(message);
    }

    public AudioFormatNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AudioFormatNotSupportedException(Throwable cause) {
        super(cause);
    }

    protected AudioFormatNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
